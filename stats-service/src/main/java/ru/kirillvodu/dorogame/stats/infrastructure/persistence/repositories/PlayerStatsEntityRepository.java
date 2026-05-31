package ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities.PlayerStatsEntity;

import java.util.Optional;
import java.util.UUID;

public interface PlayerStatsEntityRepository extends JpaRepository<PlayerStatsEntity, UUID> {
    Optional<PlayerStatsEntity> findByUserId(UUID userId);
}
