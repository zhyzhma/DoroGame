package ru.kirillvodu.dorogame.game.application.abstractions.repositories;

import ru.kirillvodu.dorogame.game.domain.model.history.ChipMove;

import java.util.List;
import java.util.UUID;

public interface ChipMoveRepository {
    List<ChipMove> getByGameId(UUID gameId);
    ChipMove save(ChipMove chipMove);
    void deleteById(UUID id);
}
