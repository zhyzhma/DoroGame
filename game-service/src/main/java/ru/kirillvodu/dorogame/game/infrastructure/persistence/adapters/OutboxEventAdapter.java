package ru.kirillvodu.dorogame.game.infrastructure.persistence.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.game.application.abstractions.events.GameEventPublisher;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.OutboxEventEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.OutboxEventEntityRepository;

import java.util.Map;

@Repository
public class OutboxEventAdapter implements GameEventPublisher {

    private final OutboxEventEntityRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxEventAdapter(OutboxEventEntityRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishGameFinished(DoroGame game) {
        Map<String, String> payload = Map.of(
                "gameId", game.getId().toString(),
                "winnerId", game.getWinner() == 1
                        ? game.getPlayer1().getUser().id().toString()
                        : game.getPlayer2().getUser().id().toString(),
                "loserId", game.getWinner() == 1
                        ? game.getPlayer2().getUser().id().toString()
                        : game.getPlayer1().getUser().id().toString()
        );

        try {
            OutboxEventEntity event = OutboxEventEntity.builder()
                    .eventType("game.finished")
                    .aggregateId(game.getId().toString())
                    .payload(objectMapper.writeValueAsString(payload))
                    .build();
            repository.save(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize game.finished event", e);
        }
    }
}
