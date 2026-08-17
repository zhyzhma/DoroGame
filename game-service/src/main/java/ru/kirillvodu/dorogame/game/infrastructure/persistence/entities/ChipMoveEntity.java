package ru.kirillvodu.dorogame.game.infrastructure.persistence.entities;

import jakarta.persistence.*;
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
public class ChipMoveEntity extends BaseEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chip_id", nullable = false)
    private ChipEntity chipEntity;

    @Column(name = "move_idx", nullable = false)
    private int moveIdx;

    @Column(name = "x_after", nullable = false)
    private int xAfter;

    @Column(name = "y_after", nullable = false)
    private int yAfter;
}
