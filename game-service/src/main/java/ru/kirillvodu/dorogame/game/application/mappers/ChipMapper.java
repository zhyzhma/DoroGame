package ru.kirillvodu.dorogame.game.application.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.ChipDTO;
import ru.kirillvodu.dorogame.game.domain.model.Chip;

import java.util.List;

@Component
public class ChipMapper {
    public ChipDTO toDTO(Chip chip) {
        return new ChipDTO(chip.getId(), chip.getCoords().x(), chip.getCoords().y());
    }

    public List<ChipDTO> toDTO(List<Chip> chips) {
        return chips.stream().map(this::toDTO).toList();
    }
}
