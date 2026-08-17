package ru.kirillvodu.dorogame.game.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kirillvodu.dorogame.game.application.abstractions.events.GameEventPublisher;
import ru.kirillvodu.dorogame.game.application.abstractions.events.GameUpdateNotifier;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.ChipMoveRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.contracts.commands.MakeMoveCommand;
import ru.kirillvodu.dorogame.game.application.contracts.results.MoveResult;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.domain.model.Coords;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock private DoroGameRepository doroGameRepository;
    @Mock private ChipMoveRepository chipMoveRepository;
    @Mock private GameEventPublisher gameEventPublisher;
    @Mock private GameUpdateNotifier gameUpdateNotifier;

    @InjectMocks private GameService gameService;

    @Test
    void makeMove_returnsMoveResultWithoutPublishing_whenGameNotFinished() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        UUID chipId = UUID.randomUUID();
        DoroGame game = mock(DoroGame.class);
        when(game.isFinished()).thenReturn(false);
        when(game.getWinner()).thenReturn(0);
        when(game.getTurn()).thenReturn(2);
        when(doroGameRepository.getById(gameId)).thenReturn(Optional.of(game));
        when(doroGameRepository.save(game)).thenReturn(game);

        // Act
        MoveResult result = gameService.makeMove(new MakeMoveCommand(gameId, playerId, chipId, new Coords(1, 1)));

        // Assert
        assertThat(result.finished()).isFalse();
        assertThat(result.turn()).isEqualTo(2);
        verify(game).makeMove(playerId, chipId, new Coords(1, 1));
        verify(doroGameRepository).save(game);
        verifyNoInteractions(gameEventPublisher);
    }

    @Test
    void makeMove_publishesGameFinishedEvent_whenGameFinished() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        DoroGame game = mock(DoroGame.class);
        when(game.isFinished()).thenReturn(true);
        when(game.getWinner()).thenReturn(1);
        when(game.getTurn()).thenReturn(0);
        when(doroGameRepository.getById(gameId)).thenReturn(Optional.of(game));
        when(doroGameRepository.save(game)).thenReturn(game);

        // Act
        gameService.makeMove(new MakeMoveCommand(gameId, UUID.randomUUID(), UUID.randomUUID(), new Coords(1, 1)));

        // Assert
        verify(gameEventPublisher).publishGameFinished(game);
    }

    @Test
    void makeMove_throwsObjectNotFoundException_whenGameNotFound() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        when(doroGameRepository.getById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class,
                () -> gameService.makeMove(new MakeMoveCommand(gameId, UUID.randomUUID(), UUID.randomUUID(), new Coords(0, 0))));
    }
}
