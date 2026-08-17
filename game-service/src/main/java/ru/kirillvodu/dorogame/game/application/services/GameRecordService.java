package ru.kirillvodu.dorogame.game.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.ChipMoveRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.contracts.commands.GetGameRecordCommand;
import ru.kirillvodu.dorogame.game.application.exceptions.GameNotFinishedException;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.domain.model.ChipMove;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.GameRecord;

import java.util.List;

@Service
public class GameRecordService {
    @Autowired
    private ChipMoveRepository chipMoveRepository;

    @Autowired
    private DoroGameRepository doroGameRepository;

    public GameRecord getGameRecord(GetGameRecordCommand command) {

        DoroGame game = doroGameRepository.getById(command.gameId())
                .orElseThrow(() -> new ObjectNotFoundException(command.gameId(), "DoroGame"));
        if (!command.userId().equals(game.getPlayer1().getUser().id())
                && !command.userId().equals(game.getPlayer2().getUser().id())) {
            throw new ObjectNotFoundException(command.gameId(), "DoroGame");
        }
        if (!game.isFinished()) {
            throw new GameNotFinishedException(command.gameId());
        }
        List<ChipMove> chipMoves = chipMoveRepository.getByGameId(command.gameId());
        return new GameRecord(game, chipMoves);
    }
}
