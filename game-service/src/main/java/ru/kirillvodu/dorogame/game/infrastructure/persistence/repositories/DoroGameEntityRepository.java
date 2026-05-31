package ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.DoroGameEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoroGameEntityRepository extends JpaRepository<DoroGameEntity, UUID> {
    List<DoroGameEntity> findAllByRemovedFalse();
    Optional<DoroGameEntity> findByIdAndRemovedFalse(UUID id);
}
