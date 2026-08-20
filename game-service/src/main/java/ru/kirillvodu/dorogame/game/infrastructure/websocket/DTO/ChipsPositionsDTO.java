package ru.kirillvodu.dorogame.game.infrastructure.websocket.DTO;

import ru.kirillvodu.dorogame.game.domain.model.game.Chip;

import java.util.List;

public record ChipsPositionsDTO(
    List<Chip> chipsPlayer1,
    List<Chip> chipsPlayer2
)
{}
