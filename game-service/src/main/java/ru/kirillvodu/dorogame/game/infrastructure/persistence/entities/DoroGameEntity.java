package ru.kirillvodu.dorogame.game.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "doro_games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoroGameEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "player1_id", nullable = false)
    private UUID player1Id;

    @Column(name = "player1_name", nullable = false)
    private String player1Name;

    @Column(name = "player2_id", nullable = false)
    private UUID player2Id;

    @Column(name = "player2_name", nullable = false)
    private String player2Name;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_variant", nullable = false)
    private FieldVariant fieldVariant;

    @Enumerated(EnumType.STRING)
    @Column(name = "win_checker_variant", nullable = false)
    private WinCheckerVariant winCheckerVariant;

    @Column(name = "finished", nullable = false)
    private boolean finished;

    @Column(name = "winner", nullable = false)
    private int winner;

    @Column(name = "turn", nullable = false)
    private int turn;

    @Column(name = "move_counter", nullable = false)
    private int moveCounter;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "game_id", nullable = false)
    private List<ChipEntity> startPositions;
}
