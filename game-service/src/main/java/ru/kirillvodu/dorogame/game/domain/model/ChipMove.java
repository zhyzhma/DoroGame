package ru.kirillvodu.dorogame.game.domain.model;

import java.util.UUID;

public record ChipMove(UUID id, Chip chip, int moveIndex) {

    public ChipMove(Chip chip, int moveIndex) {
        this(UUID.randomUUID(), chip, moveIndex);
    }
}
