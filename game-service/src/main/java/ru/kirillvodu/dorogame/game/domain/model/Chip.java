package ru.kirillvodu.dorogame.game.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Chip {
    private UUID id;
    @Setter
    private Coords coords;

    public Chip(UUID id, Coords coords) {
        this.id = id;
        this.coords = coords;
    }

    public Chip(Coords coords) {
        this.id = UUID.randomUUID();
        this.coords = coords;
    }
}
