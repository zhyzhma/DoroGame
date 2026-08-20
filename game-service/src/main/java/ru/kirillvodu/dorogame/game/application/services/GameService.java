package ru.kirillvodu.dorogame.game.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kirillvodu.dorogame.game.application.abstractions.events.GameEventPublisher;
import ru.kirillvodu.dorogame.game.application.abstractions.events.GameUpdateNotifier;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.ChipMoveRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.contracts.commands.MakeMoveCommand;
import ru.kirillvodu.dorogame.game.application.contracts.results.MoveResult;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.domain.model.game.Chip;
import ru.kirillvodu.dorogame.game.domain.model.history.ChipMove;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private DoroGameRepository doroGameRepository;
    @Autowired
    private ChipMoveRepository chipMoveRepository;
    @Autowired
    private GameEventPublisher gameEventPublisher;
    @Autowired
    private GameUpdateNotifier gameUpdateNotifier;

    public List<DoroGame> getByUserIdAndFinishedTrue(UUID userId) {
        return doroGameRepository.getByUserIdAndFinishedTrue(userId);
    }

    public MoveResult makeMove(MakeMoveCommand command) {
        DoroGame game = doroGameRepository.getById(command.gameId())
                .orElseThrow(() -> new ObjectNotFoundException(command.gameId(), "DoroGame"));

        Chip chip = game.makeMove(command.playerId(), command.chipId(), command.coords());

        chipMoveRepository.save(new ChipMove(chip, game.getMoveCounter()));
        doroGameRepository.save(game);

        gameUpdateNotifier.notifyGameUpdated(game);

        if (game.isFinished()) {
            gameEventPublisher.publishGameFinished(game);
        }

        return new MoveResult(game.isFinished(), game.getWinner(), game.getTurn());
    }
}
