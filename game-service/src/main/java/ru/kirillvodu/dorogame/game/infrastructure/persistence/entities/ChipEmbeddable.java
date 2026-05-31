package ru.kirillvodu.dorogame.game.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChipEmbeddable {

    @Column(name = "chip_id", nullable = false)
    private UUID chipId;

    @Column(name = "coord_x", nullable = false)
    private int coordX;

    @Column(name = "coord_y", nullable = false)
    private int coordY;
}
