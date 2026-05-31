package ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.OutboxEventEntity;

import java.util.List;
import java.util.UUID;

public interface OutboxEventEntityRepository extends JpaRepository<OutboxEventEntity, UUID> {
    List<OutboxEventEntity> findAllBySentFalse();
}
