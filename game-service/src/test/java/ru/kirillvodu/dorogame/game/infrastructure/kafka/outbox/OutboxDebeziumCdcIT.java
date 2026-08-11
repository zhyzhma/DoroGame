package ru.kirillvodu.dorogame.game.infrastructure.kafka.outbox;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OutboxDebeziumCdcIT {

    private static final Network network = Network.newNetwork();

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("gamedb_test")
            .withUsername("user")
            .withPassword("password")
            .withCommand("postgres", "-c", "wal_level=logical")
            .withInitScript("debezium/init-debezium-user.sql")
            .withNetwork(network)
            .withNetworkAliases("postgres-game");

    private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"))
            .withNetwork(network)
            .withNetworkAliases("kafka")
            .withListener("kafka:19092");

    private static final GenericContainer<?> kafkaConnect = new GenericContainer<>(DockerImageName.parse("debezium/connect:2.7.3.Final"))
            .withNetwork(network)
            .withNetworkAliases("kafka-connect")
            .withExposedPorts(8083)
            .withEnv("BOOTSTRAP_SERVERS", "kafka:19092")
            .withEnv("GROUP_ID", "1")
            .withEnv("CONFIG_STORAGE_TOPIC", "dorogame_connect_configs")
            .withEnv("OFFSET_STORAGE_TOPIC", "dorogame_connect_offsets")
            .withEnv("STATUS_STORAGE_TOPIC", "dorogame_connect_status")
            .withEnv("KEY_CONVERTER", "org.apache.kafka.connect.json.JsonConverter")
            .withEnv("VALUE_CONVERTER", "org.apache.kafka.connect.json.JsonConverter")
            .withEnv("CONNECT_KEY_CONVERTER_SCHEMAS_ENABLE", "false")
            .withEnv("CONNECT_VALUE_CONVERTER_SCHEMAS_ENABLE", "false")
            .withEnv("CONNECT_OFFSET_FLUSH_INTERVAL_MS", "1000")
            .withEnv("CONNECT_CONFIG_PROVIDERS", "env")
            .withEnv("CONNECT_CONFIG_PROVIDERS_ENV_CLASS", "org.apache.kafka.common.config.provider.EnvVarConfigProvider")
            .withEnv("DEBEZIUM_USER", "debezium_test")
            .withEnv("DEBEZIUM_PASSWORD", "debezium_test")
            .withEnv("POSTGRES_DB", "gamedb_test")
            .waitingFor(Wait.forHttp("/connectors").forStatusCode(200).withStartupTimeout(Duration.ofMinutes(3)));

    @BeforeAll
    static void startInfraAndRegisterConnector() throws Exception {
        postgres.start();
        kafka.start();
        kafkaConnect.start();
        kafkaConnect.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger("kafka-connect")));

        runLiquibase();
        createOutputTopic();
        registerDebeziumConnector();
    }

    @AfterAll
    static void stopInfra() {
        kafkaConnect.stop();
        kafka.stop();
        postgres.stop();
        network.close();
    }

    private static void createOutputTopic() throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafka.getBootstrapServers());
        try (Admin admin = Admin.create(props)) {
            admin.createTopics(List.of(new NewTopic("orders.events.game.finished", 1, (short) 1)))
                    .all().get();
        }
    }

    private static void runLiquibase() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.getChangeLogParameters().set("debeziumUser", "debezium_test");
            liquibase.update("");
        }
    }

    private static void registerDebeziumConnector() throws Exception {
        String connectorConfig = Files.readString(
                new ClassPathResource("debezium/connector-config.json").getFile().toPath());

        String connectUrl = "http://" + kafkaConnect.getHost() + ":" + kafkaConnect.getMappedPort(8083);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        new RestTemplate().put(connectUrl + "/connectors/postgres-connector/config",
                new HttpEntity<>(connectorConfig, headers));
    }

    @Test
    void insertOutboxRow_isCapturedAndRoutedToKafka_whenDebeziumConnectorActive() throws Exception {
        // Arrange
        UUID eventId = UUID.randomUUID();
        UUID aggregateId = UUID.randomUUID();
        String payload = "{\"gameId\":\"" + aggregateId + "\"}";

        // Act
        try (Connection connection = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            try (PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO outbox_events (id, created_at, updated_at, removed, event_type, aggregate_id, payload, sent)
                    VALUES (?, ?, ?, false, ?, ?, ?, false)
                    """)) {
                statement.setObject(1, eventId);
                statement.setObject(2, Timestamp.from(Instant.now()));
                statement.setObject(3, Timestamp.from(Instant.now()));
                statement.setString(4, "game.finished");
                statement.setString(5, aggregateId.toString());
                statement.setString(6, payload);
                statement.executeUpdate();
            }
        }

        // Assert
        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> {
                    var result = kafka.execInContainer("/opt/kafka/bin/kafka-console-consumer.sh",
                            "--bootstrap-server", "kafka:19092",
                            "--topic", "orders.events.game.finished",
                            "--from-beginning", "--max-messages", "1", "--timeout-ms", "5000",
                            "--property", "print.key=true");
                    assertThat(result.getStdout()).contains(aggregateId.toString());
                });
    }
}
