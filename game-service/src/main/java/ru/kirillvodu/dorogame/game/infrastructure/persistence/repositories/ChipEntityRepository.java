package ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipEntity;

import java.util.UUID;

public interface ChipEntityRepository  extends JpaRepository<ChipEntity, UUID> {
}
