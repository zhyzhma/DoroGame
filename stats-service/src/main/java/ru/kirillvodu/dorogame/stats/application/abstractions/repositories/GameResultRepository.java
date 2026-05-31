package ru.kirillvodu.dorogame.stats.application.abstractions.repositories;

import ru.kirillvodu.dorogame.stats.domain.model.GameResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameResultRepository {
    GameResult save(GameResult result);
    Optional<GameResult> getById(UUID id);
    List<GameResult> getByUserId(UUID userId);
}
