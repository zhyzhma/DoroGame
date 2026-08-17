package ru.kirillvodu.dorogame.game.infrastructure.persistence.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.domain.model.Chip;
import ru.kirillvodu.dorogame.game.domain.model.Coords;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;
import ru.kirillvodu.dorogame.game.domain.model.field.Field;
import ru.kirillvodu.dorogame.game.domain.model.winchecker.WinChecker;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipMoveEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.DoroGameEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DoroGameEntityMapper {

    public DoroGame toDomain(DoroGameEntity entity, Field field, WinChecker winChecker, List<ChipMoveEntity> lastMoves) {
        UserReadModel p1 = new UserReadModel(entity.getPlayer1Id(), entity.getPlayer1Name());
        UserReadModel p2 = new UserReadModel(entity.getPlayer2Id(), entity.getPlayer2Name());

        Map<UUID, ChipMoveEntity> lastMoveByChip = lastMoves.stream()
                .collect(Collectors.toMap(m -> m.getChipEntity().getId(), m -> m));

        List<Chip> chips1 = entity.getStartPositions().stream()
                .filter(c -> c.getPlayerNumber() == 1)
                .map(c -> toChip(c, lastMoveByChip))
                .toList();
        List<Chip> chips2 = entity.getStartPositions().stream()
                .filter(c -> c.getPlayerNumber() == 2)
                .map(c -> toChip(c, lastMoveByChip))
                .toList();

        return new DoroGame(entity.getId(), p1, p2, field, winChecker, chips1, chips2,
                entity.isFinished(), entity.getWinner(), entity.getTurn(), entity.getMoveCounter());
    }

    private ChipEntity toChipEntity(Chip chip, int playerNumber) {
        ChipEntity entity = ChipEntity.builder()
                .coordX(chip.getCoords().x())
                .coordY(chip.getCoords().y())
                .playerNumber(playerNumber)
                .build();
        entity.setId(chip.getId());
        return entity;
    }

    private Chip toChip(ChipEntity start, Map<UUID, ChipMoveEntity> lastMoveByChip) {
        ChipMoveEntity last = lastMoveByChip.get(start.getId());
        Coords coords = last != null
                ? new Coords(last.getXAfter(), last.getYAfter())
                : new Coords(start.getCoordX(), start.getCoordY());
        return new Chip(start.getId(), coords);
    }

    public DoroGameEntity fromDomain(DoroGame game) {
        List<ChipEntity> chips1 = game.getPlayer1().getChips().stream()
                .map(c -> toChipEntity(c, 1))
                .toList();
        List<ChipEntity> chips2 = game.getPlayer2().getChips().stream()
                .map(c -> toChipEntity(c, 2))
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
                .startPositions(Stream.concat(chips1.stream(), chips2.stream()).toList())
                .build();

        entity.setId(game.getId());
        return entity;
    }
}
