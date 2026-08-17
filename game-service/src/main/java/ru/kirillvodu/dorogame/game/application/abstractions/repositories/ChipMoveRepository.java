package ru.kirillvodu.dorogame.game.application.abstractions.repositories;

import ru.kirillvodu.dorogame.game.domain.model.Chip;
import ru.kirillvodu.dorogame.game.domain.model.ChipMove;
import ru.kirillvodu.dorogame.game.domain.model.Coords;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChipMoveRepository {
    List<ChipMove> getByGameId(UUID gameId);
    ChipMove save(ChipMove chipMove);
    void deleteById(UUID id);
}
