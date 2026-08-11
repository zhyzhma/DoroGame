package ru.kirillvodu.dorogame.game.infrastructure.kafka.outbox;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import ru.kirillvodu.dorogame.game.IntegrationTestBase;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.OutboxEventEntity;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class OutboxEventPublisherIT extends IntegrationTestBase {

    @Test
    void publishPendingEvents_publishesAndMarksSent_whenRealKafkaAvailable() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        String payload = """
                {"gameId":"%s","winnerId":"%s","loserId":"%s"}
                """.formatted(gameId, UUID.randomUUID(), UUID.randomUUID());

        OutboxEventEntity event = OutboxEventEntity.builder()
                .eventType("game.finished")
                .aggregateId(gameId.toString())
                .payload(payload)
                .sent(false)
                .build();
        outboxRepository.save(event);

        // Act & Assert
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps())) {
            consumer.subscribe(List.of("game.finished"));

            Awaitility.await()
                    .atMost(Duration.ofSeconds(15))
                    .untilAsserted(() -> {
                        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                        List<ConsumerRecord<String, String>> matching = StreamSupport
                                .stream(records.records("game.finished").spliterator(), false)
                                .filter(r -> gameId.toString().equals(r.key()))
                                .toList();
                        assertThat(matching).isNotEmpty();
                        assertThat(matching.get(0).value()).contains(gameId.toString());
                    });
        }

        Awaitility.await()
                .atMost(Duration.ofSeconds(15))
                .untilAsserted(() ->
                        assertThat(outboxRepository.findById(event.getId()).orElseThrow().isSent()).isTrue());
    }

    private Properties consumerProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "outbox-it-" + UUID.randomUUID());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }
}
