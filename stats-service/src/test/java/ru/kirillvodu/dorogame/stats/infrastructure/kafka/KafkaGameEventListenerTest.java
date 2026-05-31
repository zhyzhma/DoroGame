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
    void onGameFinished_validMessage_callsRecordGameResult() throws Exception {
        UUID gameId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();

        String message = objectMapper.writeValueAsString(
                new ru.kirillvodu.dorogame.stats.application.contracts.events.GameFinishedEvent(
                        gameId.toString(), winnerId.toString(), loserId.toString()));

        listener.onGameFinished(message);

        verify(statsService).recordGameResult(gameId, winnerId, loserId);
    }

    @Test
    void onGameFinished_invalidJson_doesNotThrow() {
        listener.onGameFinished("not-valid-json");

        verifyNoInteractions(statsService);
    }

    @Test
    void onGameFinished_missingFields_doesNotThrow() throws Exception {
        String message = objectMapper.writeValueAsString(
                new ru.kirillvodu.dorogame.stats.application.contracts.events.GameFinishedEvent(
                        null, null, null));

        listener.onGameFinished(message);

        verifyNoInteractions(statsService);
    }
}
