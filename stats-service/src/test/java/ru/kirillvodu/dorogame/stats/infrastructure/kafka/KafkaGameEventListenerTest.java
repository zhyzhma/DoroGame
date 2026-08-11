package ru.kirillvodu.dorogame.stats.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kirillvodu.dorogame.stats.application.services.StatsService;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaGameEventListenerTest {

    @Mock private StatsService statsService;

    private KafkaGameEventListener listener;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        listener = new KafkaGameEventListener(statsService, objectMapper);
    }

    @Test
    void onGameFinished_callsRecordGameResult_whenMessageValid() throws Exception {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();

        String message = objectMapper.writeValueAsString(
                new ru.kirillvodu.dorogame.stats.application.contracts.events.GameFinishedEvent(
                        gameId.toString(), winnerId.toString(), loserId.toString()));

        // Act
        listener.onGameFinished(message);

        // Assert
        verify(statsService).recordGameResult(gameId, winnerId, loserId);
    }

    @Test
    void onGameFinished_doesNotThrow_whenJsonInvalid() {
        // Arrange & Act
        listener.onGameFinished("not-valid-json");

        // Assert
        verifyNoInteractions(statsService);
    }

    @Test
    void onGameFinished_doesNotThrow_whenFieldsMissing() throws Exception {
        // Arrange
        String message = objectMapper.writeValueAsString(
                new ru.kirillvodu.dorogame.stats.application.contracts.events.GameFinishedEvent(
                        null, null, null));

        // Act
        listener.onGameFinished(message);

        // Assert
        verifyNoInteractions(statsService);
    }
}
