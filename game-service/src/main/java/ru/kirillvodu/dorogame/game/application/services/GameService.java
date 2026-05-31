package ru.kirillvodu.dorogame.game.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kirillvodu.dorogame.game.application.abstractions.events.GameEventPublisher;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.contracts.commands.MakeMoveCommand;
import ru.kirillvodu.dorogame.game.application.contracts.results.MoveResult;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;

import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private DoroGameRepository doroGameRepository;
    @Autowired
    private GameEventPublisher gameEventPublisher;

    public DoroGame getGame(UUID gameId) {
        return doroGameRepository.getById(gameId)
                .orElseThrow(() -> new ObjectNotFoundException(gameId, "DoroGame"));
    }

    public MoveResult makeMove(MakeMoveCommand command) {
        DoroGame game = doroGameRepository.getById(command.gameId())
                .orElseThrow(() -> new ObjectNotFoundException(command.gameId(), "DoroGame"));

        game.makeMove(command.playerId(), command.chipId(), command.coords());

        doroGameRepository.save(game);

        if (game.isFinished()) {
            gameEventPublisher.publishGameFinished(game);
        }

        return new MoveResult(game.isFinished(), game.getWinner(), game.getTurn());
    }
}
