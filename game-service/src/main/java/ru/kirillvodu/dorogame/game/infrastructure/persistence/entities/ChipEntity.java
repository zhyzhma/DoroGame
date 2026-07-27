package ru.kirillvodu.dorogame.game.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChipEntity extends BaseEntity {
    @Column(name = "coord_x", nullable = false)
    private int coordX;

    @Column(name = "coord_y", nullable = false)
    private int coordY;

    @Column(name = "player_number", nullable = false)
    private int playerNumber;

    public ChipEntity(UUID id, int coordX, int coordY, int playerNumber) {
        this.setId(id);
        this.coordX = coordX;
        this.coordY = coordY;
        this.playerNumber = playerNumber;
    }
}
