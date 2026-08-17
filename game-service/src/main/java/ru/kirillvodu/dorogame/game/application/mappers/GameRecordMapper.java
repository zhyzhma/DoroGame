package ru.kirillvodu.dorogame.game.application.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.GameRecordDTO;
import ru.kirillvodu.dorogame.game.domain.model.ChipMove;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.GameRecord;

import java.util.Comparator;
import java.util.List;

@Component
public class GameRecordMapper {
    @Autowired
    private ChipMoveMapper chipMoveMapper;

    @Autowired
    private ChipMapper chipMapper;

    public GameRecordDTO toDTO(GameRecord gameRecord) {
        DoroGame game = gameRecord.game();
        List<ChipMove> sortedMoves = gameRecord.chipMoves().stream()
                .sorted(Comparator.comparingInt(ChipMove::moveIndex))
                .toList();

        return GameRecordDTO.builder()
                .id(game.getId())
                .player1Id(game.getPlayer1().getUser().id())
                .player1Name(game.getPlayer1().getUser().name())
                .player2Id(game.getPlayer2().getUser().id())
                .player2Name(game.getPlayer2().getUser().name())
                .startPositions1(chipMapper.toDTO(game.getPlayer1().getChips()))
                .startPositions2(chipMapper.toDTO(game.getPlayer2().getChips()))
                .winner(game.getWinner())
                .chipMoves(chipMoveMapper.toDTO(sortedMoves))
                .build();
    }
}
