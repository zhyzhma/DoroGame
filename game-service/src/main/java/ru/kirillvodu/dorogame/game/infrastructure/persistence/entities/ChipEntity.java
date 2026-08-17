package ru.kirillvodu.dorogame.game.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "start_positions")
public class ChipEntity extends BaseEntity {
    @Id
    private UUID id;

    @Column(name = "coord_x", nullable = false)
    private int coordX;

    @Column(name = "coord_y", nullable = false)
    private int coordY;

    @Column(name = "player_number", nullable = false)
    private int playerNumber;

    @Column(name = "game_id", insertable = false, updatable = false)
    private UUID gameId;
}
