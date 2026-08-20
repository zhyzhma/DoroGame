package ru.kirillvodu.dorogame.game.domain.model.game;

import lombok.Getter;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;
import ru.kirillvodu.dorogame.game.domain.model.game.field.Field;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinChecker;

import java.util.List;
import java.util.UUID;

@Getter
public class DoroGame {
    private UUID id;
    private final Player player1;
    private final Player player2;
    private final Field field;
    private final WinChecker winChecker;
    private boolean isFinished;
    private int winner;
    private int turn;
    private int moveCounter;

    public DoroGame(UUID id,
                    UserReadModel player1,
                    UserReadModel player2,
                    Field field,
                    WinChecker winChecker,
                    List<Chip> chipsPlayer1,
                    List<Chip> chipsPlayer2,
                    boolean isFinished,
                    int winner,
                    int turn,
                    int moveCounter) {
        if (turn > 2 || turn < 1) {
            throw new IllegalArgumentException("Invalid turn: must be 1 or 2");
        }
        this.id = id;
        this.player1 = new Player(player1, chipsPlayer1);
        this.player2 = new Player(player2, chipsPlayer2);
        this.field = field;
        this.winChecker = winChecker;
        this.isFinished = isFinished;
        this.winner = winner;
        this.turn = turn;
        this.moveCounter = moveCounter;
    }

    public DoroGame(UUID id,
                    UserReadModel player1,
                    UserReadModel player2,
                    Field field,
                    WinChecker winChecker,
                    List<Chip> chipsPlayer1,
                    List<Chip> chipsPlayer2,
                    int turn) {
        this(id, player1, player2, field, winChecker, chipsPlayer1, chipsPlayer2, false, 0, turn, 0);
    }

    public DoroGame(UserReadModel player1,
                    UserReadModel player2,
                    Field field,
                    WinChecker winChecker,
                    List<Chip> chipsPlayer1,
                    List<Chip> chipsPlayer2,
                    int turn) {
        this(null, player1, player2, field, winChecker, chipsPlayer1, chipsPlayer2, false, 0, turn, 0);
    }

    public Chip makeMove(UUID playerId, UUID chipId, Coords coords) {
        if (isFinished) {
            throw new IllegalStateException("Game is finished");
        }
        Player current = (turn == 1) ? player1 : player2;
        if (!current.getUser().id().equals(playerId)) {
            throw new IllegalStateException("Not your turn");
        }
        if (!field.areCoordsValid(coords)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        if (player1.isCellOccupied(coords) || player2.isCellOccupied(coords)) {
            throw new IllegalStateException("Cell is occupied");
        }
        if (!field.getReachableFrom(coords, List.of(player1, player2)).contains(coords)) {
            throw new IllegalStateException("Coordinates not reachable");
        }

        Chip chip = current.moveChip(chipId, coords);
        moveCounter++;

        if (winChecker.checkWin(player1.getChips().stream().map(Chip::getCoords).toList())) {
            isFinished = true;
            winner = 1;
            turn = 0;
        } else if (winChecker.checkWin(player2.getChips().stream().map(Chip::getCoords).toList())) {
            isFinished = true;
            winner = 2;
            turn = 0;
        } else {
            turn = (turn == 1) ? 2 : 1;
        }
        return chip;
    }
}
