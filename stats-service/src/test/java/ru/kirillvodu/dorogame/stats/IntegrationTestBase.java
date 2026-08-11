package ru.kirillvodu.dorogame.stats;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories.GameResultEntityRepository;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories.PlayerStatsEntityRepository;

import java.time.Duration;
import java.util.UUID;

@SpringBootTest(classes = StatsServiceApp.class)
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

    private static final String REALM = "dorogame_realm";

    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("statsdb_test")
            .withUsername("user")
            .withPassword("password");

    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"));

    static final GenericContainer<?> keycloak = new GenericContainer<>(DockerImageName.parse("quay.io/keycloak/keycloak:24.0.0"))
            .withCommand("start-dev", "--import-realm")
            .withEnv("KEYCLOAK_ADMIN", "admin")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
            .withExposedPorts(8080)
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("keycloak/realm-export.json"),
                    "/opt/keycloak/data/import/realm-export.json")
            .waitingFor(Wait.forHttp("/realms/" + REALM).forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(3)));

    static String keycloakBaseUrl;
    static KeycloakTestSupport keycloakTestSupport;

    static {
        postgres.start();
        kafka.start();
        keycloak.start();
        keycloakBaseUrl = "http://" + keycloak.getHost() + ":" + keycloak.getMappedPort(8080);
        keycloakTestSupport = new KeycloakTestSupport(keycloakBaseUrl, REALM);
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakBaseUrl + "/realms/" + REALM);
    }

    @Autowired protected GameResultEntityRepository gameResultRepository;
    @Autowired protected PlayerStatsEntityRepository playerStatsRepository;

    protected String authToken;

    @BeforeEach
    void cleanDatabase() {
        gameResultRepository.deleteAll();
        playerStatsRepository.deleteAll();
    }

    @BeforeEach
    void provisionAuthenticatedUser() {
        String username = "it-user-" + UUID.randomUUID();
        String password = "test-password";
        String adminToken = keycloakTestSupport.fetchMasterAdminToken();
        keycloakTestSupport.createRealmUser(adminToken, username, password);
        authToken = keycloakTestSupport.fetchUserToken(username, password);
    }

    protected MockHttpServletRequestBuilder authed(MockHttpServletRequestBuilder builder) {
        return builder.header("Authorization", "Bearer " + authToken);
    }
}
