package ru.kirillvodu.dorogame.game.application.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.ChipMoveDTO;
import ru.kirillvodu.dorogame.game.domain.model.ChipMove;

import java.util.List;

@Component
public class ChipMoveMapper {
    public ChipMoveDTO toDTO(ChipMove chipMove) {
        return new ChipMoveDTO(chipMove.id(), chipMove.chip().getCoords().x(), chipMove.chip().getCoords().y());
    }

    public List<ChipMoveDTO> toDTO(List<ChipMove> chips) {
        return chips.stream().map(this::toDTO).toList();
    }
}
