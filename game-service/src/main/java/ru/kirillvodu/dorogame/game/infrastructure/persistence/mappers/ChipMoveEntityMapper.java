package ru.kirillvodu.dorogame.game.infrastructure.persistence.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.domain.model.Chip;
import ru.kirillvodu.dorogame.game.domain.model.ChipMove;
import ru.kirillvodu.dorogame.game.domain.model.Coords;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipMoveEntity;

@Component
public class ChipMoveEntityMapper {

    public ChipMove toDomain(ChipMoveEntity entity) {
        return new ChipMove(entity.getId(),
                new Chip(entity.getChipEntity().getId(), new Coords(entity.getXAfter(), entity.getYAfter())),
                entity.getMoveIdx());
    }

    public ChipMoveEntity toEntity(ChipMove chipMove, ChipEntity chipEntity) {
        ChipMoveEntity entity = new ChipMoveEntity();
        entity.setId(chipMove.id());
        entity.setChipEntity(chipEntity);
        entity.setMoveIdx(chipMove.moveIndex());
        entity.setXAfter(chipMove.chip().getCoords().x());
        entity.setYAfter(chipMove.chip().getCoords().y());
        return entity;
    }
}
