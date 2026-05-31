package ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.InvitationEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationEntityRepository extends JpaRepository<InvitationEntity, UUID> {
    List<InvitationEntity> findAllByRemovedFalse();
    Optional<InvitationEntity> findByIdAndRemovedFalse(UUID id);
}
