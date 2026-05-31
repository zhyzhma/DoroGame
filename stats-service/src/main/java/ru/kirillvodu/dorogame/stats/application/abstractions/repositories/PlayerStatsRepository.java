package ru.kirillvodu.dorogame.stats.application.abstractions.repositories;

import ru.kirillvodu.dorogame.stats.domain.model.PlayerStats;

import java.util.Optional;
import java.util.UUID;

public interface PlayerStatsRepository {
    PlayerStats save(PlayerStats stats);
    Optional<PlayerStats> getByUserId(UUID userId);
}
