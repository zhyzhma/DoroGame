package ru.kirillvodu.dorogame.game.domain.model.history;

import ru.kirillvodu.dorogame.game.domain.model.game.Chip;

import java.util.UUID;

public record ChipMove(UUID id, Chip chip, int moveIndex) {

    public ChipMove(Chip chip, int moveIndex) {
        this(UUID.randomUUID(), chip, moveIndex);
    }
}
