package ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.InvitationEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationEntityRepository extends JpaRepository<InvitationEntity, UUID> {
    List<InvitationEntity> findAllByRemovedFalse();
    Optional<InvitationEntity> findByIdAndRemovedFalse(UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE InvitationEntity e SET e.removed = true WHERE e.id = :id AND e.removed = false")
    int updateByRemovedFalse(@Param("id") UUID id);
}
