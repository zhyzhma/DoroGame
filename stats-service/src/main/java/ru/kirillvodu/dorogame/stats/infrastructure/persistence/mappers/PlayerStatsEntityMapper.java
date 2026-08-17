package ru.kirillvodu.dorogame.stats.infrastructure.persistence.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.stats.domain.model.PlayerStats;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities.PlayerStatsEntity;

@Component
public class PlayerStatsEntityMapper {

    public PlayerStats toDomain(PlayerStatsEntity entity) {
        return new PlayerStats(entity.getUserId(), entity.getWins(), entity.getLosses(), entity.getRating());
    }

    public PlayerStatsEntity fromDomain(PlayerStats stats) {
        return PlayerStatsEntity.builder()
                .userId(stats.getUserId())
                .wins(stats.getWins())
                .losses(stats.getLosses())
                .rating(stats.getRating())
                .build();
    }
}
