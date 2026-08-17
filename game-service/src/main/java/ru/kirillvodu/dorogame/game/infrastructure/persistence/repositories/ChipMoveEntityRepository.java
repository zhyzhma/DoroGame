package ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipMoveEntity;

import java.util.List;
import java.util.UUID;

public interface ChipMoveEntityRepository extends JpaRepository<ChipMoveEntity, UUID> {
    List<ChipMoveEntity> findByChipEntity_GameId(UUID gameId);

    @Query("""
        SELECT m FROM ChipMoveEntity m
        WHERE m.chipEntity.id IN :chipIds
        AND m.moveIdx = (
            SELECT MAX(m2.moveIdx) FROM ChipMoveEntity m2
            WHERE m2.chipEntity = m.chipEntity
        )
        """)
    List<ChipMoveEntity> findLastMovesForChips(@Param("chipIds") List<UUID> chipIds);
}
