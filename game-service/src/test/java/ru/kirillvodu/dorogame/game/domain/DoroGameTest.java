package ru.kirillvodu.dorogame.game.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.kirillvodu.dorogame.game.domain.model.game.field.Field;
import ru.kirillvodu.dorogame.game.domain.model.game.Chip;
import ru.kirillvodu.dorogame.game.domain.model.game.Coords;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinChecker;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DoroGameTest {

    private static final UUID PLAYER1_ID = UUID.randomUUID();
    private static final UUID PLAYER2_ID = UUID.randomUUID();

    private Field field;
    private WinChecker winChecker;
    private UUID chip1Id;

    @BeforeEach
    void setUp() {
        field = mock(Field.class);
        winChecker = mock(WinChecker.class);
        chip1Id = UUID.randomUUID();

        when(field.areCoordsValid(any())).thenReturn(true);
        when(field.getReachableFrom(any(), any()))
                .thenAnswer(inv -> List.of(inv.getArgument(0, Coords.class)));
        when(winChecker.checkWin(any())).thenReturn(false);
    }

    private DoroGame createGame() {
        UserReadModel p1 = new UserReadModel(PLAYER1_ID, "Player1");
        UserReadModel p2 = new UserReadModel(PLAYER2_ID, "Player2");
        List<Chip> chips1 = List.of(new Chip(chip1Id, new Coords(0, 0)));
        List<Chip> chips2 = List.of(new Chip(UUID.randomUUID(), new Coords(8, 0)));
        return new DoroGame(UUID.randomUUID(), p1, p2, field, winChecker, chips1, chips2, 1);
    }

    @Test
    void makeMove_switchesTurn_whenMoveValid() {
        // Arrange
        DoroGame game = createGame();

        // Act
        game.makeMove(PLAYER1_ID, chip1Id, new Coords(1, 0));

        // Assert
        assertThat(game.getTurn()).isEqualTo(2);
        assertThat(game.isFinished()).isFalse();
    }

    @Test
    void makeMove_throwsIllegalStateException_whenWrongPlayerTurn() {
        // Arrange
        DoroGame game = createGame();

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> game.makeMove(PLAYER2_ID, chip1Id, new Coords(1, 0)));
    }

    @Test
    void makeMove_throwsIllegalStateException_whenGameAlreadyFinished() {
        // Arrange
        when(winChecker.checkWin(any())).thenReturn(true);
        DoroGame game = createGame();
        game.makeMove(PLAYER1_ID, chip1Id, new Coords(1, 0));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> game.makeMove(PLAYER1_ID, chip1Id, new Coords(2, 0)));
    }

    @Test
    void makeMove_throwsIllegalArgumentException_whenCoordsInvalid() {
        // Arrange
        when(field.areCoordsValid(any())).thenReturn(false);
        DoroGame game = createGame();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> game.makeMove(PLAYER1_ID, chip1Id, new Coords(-1, -1)));
    }

    @Test
    void makeMove_throwsIllegalStateException_whenCellOccupied() {
        // Arrange
        DoroGame game = createGame();

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> game.makeMove(PLAYER1_ID, chip1Id, new Coords(8, 0)));
    }

    @Test
    void makeMove_setsFinishedAndWinner_whenPlayer1Wins() {
        // Arrange
        when(winChecker.checkWin(any())).thenReturn(true);
        DoroGame game = createGame();

        // Act
        game.makeMove(PLAYER1_ID, chip1Id, new Coords(1, 0));

        // Assert
        assertThat(game.isFinished()).isTrue();
        assertThat(game.getWinner()).isEqualTo(1);
        assertThat(game.getTurn()).isEqualTo(0);
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenTurnInvalid() {
        // Arrange
        UserReadModel p1 = new UserReadModel(PLAYER1_ID, "P1");
        UserReadModel p2 = new UserReadModel(PLAYER2_ID, "P2");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new DoroGame(p1, p2, field, winChecker, List.of(), List.of(), 3));
    }
}
