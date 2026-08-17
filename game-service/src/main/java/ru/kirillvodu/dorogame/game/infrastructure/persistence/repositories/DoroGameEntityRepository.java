package ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.DoroGameEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoroGameEntityRepository extends JpaRepository<DoroGameEntity, UUID> {
    List<DoroGameEntity> findAllByRemovedFalse();
    Optional<DoroGameEntity> findByIdAndRemovedFalse(UUID id);
    @Query("SELECT g FROM DoroGameEntity g WHERE (g.player1Id = :userId OR g.player2Id = :userId) " +
            "AND g.removed = false AND g.finished = true")
    List<DoroGameEntity> findAllByPlayerIdAndRemovedFalseAndFinishedTrue(@Param("userId") UUID userId);
}
