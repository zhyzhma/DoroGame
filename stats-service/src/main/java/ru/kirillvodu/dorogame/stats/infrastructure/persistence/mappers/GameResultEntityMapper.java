package ru.kirillvodu.dorogame.stats.infrastructure.persistence.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.stats.domain.model.GameResult;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities.GameResultEntity;

@Component
public class GameResultEntityMapper {

    public GameResult toDomain(GameResultEntity entity) {
        return new GameResult(entity.getId(), entity.getGameId(), entity.getWinnerId(),
                entity.getLoserId(), entity.getFinishedAt());
    }

    public GameResultEntity fromDomain(GameResult result) {
        GameResultEntity entity = GameResultEntity.builder()
                .gameId(result.getGameId())
                .winnerId(result.getWinnerId())
                .loserId(result.getLoserId())
                .finishedAt(result.getFinishedAt())
                .build();
        entity.setId(result.getId());
        return entity;
    }
}
