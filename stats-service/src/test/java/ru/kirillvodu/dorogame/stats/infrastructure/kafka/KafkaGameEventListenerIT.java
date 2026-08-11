package ru.kirillvodu.dorogame.stats.infrastructure.kafka;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import ru.kirillvodu.dorogame.stats.IntegrationTestBase;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaGameEventListenerIT extends IntegrationTestBase {

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void onGameFinished_recordsGameResultAndStats_whenRealKafkaMessageConsumed() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();

        String message = """
                {"gameId":"%s","winnerId":"%s","loserId":"%s"}
                """.formatted(gameId, winnerId, loserId);

        // Act
        kafkaTemplate.send("game.finished", gameId.toString(), message);

        // Assert
        Awaitility.await()
                .atMost(Duration.ofSeconds(15))
                .untilAsserted(() -> {
                    assertThat(gameResultRepository.findAllByWinnerIdOrLoserId(winnerId, winnerId)).hasSize(1);
                    assertThat(playerStatsRepository.findByUserId(winnerId)).isPresent();
                    assertThat(playerStatsRepository.findByUserId(winnerId).get().getWins()).isEqualTo(1);
                    assertThat(playerStatsRepository.findByUserId(loserId).get().getLosses()).isEqualTo(1);
                });
    }
}
