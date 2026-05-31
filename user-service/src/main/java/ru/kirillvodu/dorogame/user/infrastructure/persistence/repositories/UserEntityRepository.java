package ru.kirillvodu.dorogame.user.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.entities.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {
    List<UserEntity> findAllByRemovedFalse();
    Optional<UserEntity> findByKeycloakId(UUID keycloakId);
    List<UserEntity> findAllByKeycloakIdIn(List<UUID> keycloakIds);
}
