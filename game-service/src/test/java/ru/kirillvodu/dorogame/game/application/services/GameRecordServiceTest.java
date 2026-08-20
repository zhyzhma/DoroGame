package ru.kirillvodu.dorogame.game.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.ChipMoveRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.contracts.commands.GetGameRecordCommand;
import ru.kirillvodu.dorogame.game.application.exceptions.GameNotFinishedException;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.domain.model.game.field.Field;
import ru.kirillvodu.dorogame.game.domain.model.game.Chip;
import ru.kirillvodu.dorogame.game.domain.model.game.Coords;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinChecker;
import ru.kirillvodu.dorogame.game.domain.model.history.ChipMove;
import ru.kirillvodu.dorogame.game.domain.model.history.GameRecord;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GameRecordServiceTest {

    @Mock private ChipMoveRepository chipMoveRepository;
    @Mock private DoroGameRepository doroGameRepository;

    @InjectMocks private GameRecordService gameRecordService;

    private final UUID player1Id = UUID.randomUUID();
    private final UUID player2Id = UUID.randomUUID();
    private final Field field = mock(Field.class);
    private final WinChecker winChecker = mock(WinChecker.class);

    private DoroGame finishedGame(UUID gameId) {
        UserReadModel p1 = new UserReadModel(player1Id, "Player1");
        UserReadModel p2 = new UserReadModel(player2Id, "Player2");
        List<Chip> chips1 = List.of(new Chip(UUID.randomUUID(), new Coords(0, 0)));
        List<Chip> chips2 = List.of(new Chip(UUID.randomUUID(), new Coords(8, 0)));
        return new DoroGame(gameId, p1, p2, field, winChecker, chips1, chips2, true, 1, 1, 3);
    }

    @Test
    void getGameRecord_returnsRecord_whenGameFinishedAndUserIsParticipant() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        DoroGame game = finishedGame(gameId);
        ChipMove move = new ChipMove(game.getPlayer1().getChips().getFirst(), 0);
        when(doroGameRepository.getById(gameId)).thenReturn(Optional.of(game));
        when(chipMoveRepository.getByGameId(gameId)).thenReturn(List.of(move));

        // Act
        GameRecord result = gameRecordService.getGameRecord(new GetGameRecordCommand(gameId, player1Id));

        // Assert
        assertThat(result.game()).isSameAs(game);
        assertThat(result.chipMoves()).containsExactly(move);
    }

    @Test
    void getGameRecord_returnsRecord_whenRequestedByPlayer2() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        DoroGame game = finishedGame(gameId);
        when(doroGameRepository.getById(gameId)).thenReturn(Optional.of(game));
        when(chipMoveRepository.getByGameId(gameId)).thenReturn(List.of());

        // Act
        GameRecord result = gameRecordService.getGameRecord(new GetGameRecordCommand(gameId, player2Id));

        // Assert
        assertThat(result.game()).isSameAs(game);
    }

    @Test
    void getGameRecord_throwsObjectNotFoundException_whenGameNotFound() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        when(doroGameRepository.getById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class,
                () -> gameRecordService.getGameRecord(new GetGameRecordCommand(gameId, player1Id)));
    }

    @Test
    void getGameRecord_throwsObjectNotFoundException_whenUserNotParticipant() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        DoroGame game = finishedGame(gameId);
        when(doroGameRepository.getById(gameId)).thenReturn(Optional.of(game));

        // Act & Assert
        assertThrows(ObjectNotFoundException.class,
                () -> gameRecordService.getGameRecord(new GetGameRecordCommand(gameId, UUID.randomUUID())));
    }

    @Test
    void getGameRecord_throwsGameNotFinishedException_whenGameNotFinished() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UserReadModel p1 = new UserReadModel(player1Id, "Player1");
        UserReadModel p2 = new UserReadModel(player2Id, "Player2");
        DoroGame game = new DoroGame(gameId, p1, p2, field, winChecker, List.of(), List.of(), 1);
        when(doroGameRepository.getById(gameId)).thenReturn(Optional.of(game));

        // Act & Assert
        assertThrows(GameNotFinishedException.class,
                () -> gameRecordService.getGameRecord(new GetGameRecordCommand(gameId, player1Id)));
    }
}
