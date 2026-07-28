package ru.kirillvodu.dorogame.game.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.kirillvodu.dorogame.game.domain.model.Chip;
import ru.kirillvodu.dorogame.game.domain.model.Coords;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.domain.model.field.Field;
import ru.kirillvodu.dorogame.game.domain.model.winchecker.WinChecker;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Entity
@Table(name = "doro_games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoroGameEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "game_id", nullable = false)
    private List<ChipEntity> chips;

    public DoroGame toDomain(Field field, WinChecker winChecker) {
        UserReadModel p1 = new UserReadModel(player1Id, player1Name);
        UserReadModel p2 = new UserReadModel(player2Id, player2Name);

        List<Chip> chips1 = chips.stream()
                .filter(c -> c.getPlayerNumber() == 1)
                .map(c -> new Chip(c.getId(), new Coords(c.getCoordX(), c.getCoordY())))
                .toList();
        List<Chip> chips2 = chips.stream()
                .filter(c -> c.getPlayerNumber() == 2)
                .map(c -> new Chip(c.getId(), new Coords(c.getCoordX(), c.getCoordY())))
                .toList();

        return new DoroGame(getId(), p1, p2, field, winChecker, chips1, chips2, turn, finished, winner);
    }

    public static DoroGameEntity fromDomain(DoroGame game) {
        List<ChipEntity> chips1 = game.getPlayer1().getChips().stream()
                .map(c -> new ChipEntity(c.getId(), c.getCoords().x(), c.getCoords().y(), 1))
                .toList();
        List<ChipEntity> chips2 = game.getPlayer2().getChips().stream()
                .map(c -> new ChipEntity(c.getId(), c.getCoords().x(), c.getCoords().y(), 2))
                .toList();

        DoroGameEntity entity = DoroGameEntity.builder()
                .player1Id(game.getPlayer1().getUser().id())
                .player1Name(game.getPlayer1().getUser().name())
                .player2Id(game.getPlayer2().getUser().id())
                .player2Name(game.getPlayer2().getUser().name())
                .fieldVariant(game.getField().getFieldVariant())
                .winCheckerVariant(game.getWinChecker().getWinCheckerVariant())
                .finished(game.isFinished())
                .winner(game.getWinner())
                .turn(game.getTurn())
                .chips(Stream.concat(chips1.stream(), chips2.stream()).toList())
                .build();

        entity.setId(game.getId());
        return entity;
    }
}
