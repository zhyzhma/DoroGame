package ru.kirillvodu.dorogame.stats.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.stats.application.contracts.events.GameFinishedEvent;
import ru.kirillvodu.dorogame.stats.application.services.StatsService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaGameEventListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaGameEventListener.class);

    private final StatsService statsService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "game.finished", groupId = "stats-service-group")
    public void onGameFinished(String message) {
        try {
            GameFinishedEvent event = objectMapper.readValue(message, GameFinishedEvent.class);
            statsService.recordGameResult(
                    UUID.fromString(event.gameId()),
                    UUID.fromString(event.winnerId()),
                    UUID.fromString(event.loserId())
            );
        } catch (Exception e) {
            log.error("Failed to process game.finished event: {}", e.getMessage(), e);
        }
    }
}
