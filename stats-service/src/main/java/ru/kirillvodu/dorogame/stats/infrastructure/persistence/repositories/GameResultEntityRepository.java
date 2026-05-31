package ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities.GameResultEntity;

import java.util.List;
import java.util.UUID;

public interface GameResultEntityRepository extends JpaRepository<GameResultEntity, UUID> {
    List<GameResultEntity> findAllByWinnerIdOrLoserId(UUID winnerId, UUID loserId);
}
