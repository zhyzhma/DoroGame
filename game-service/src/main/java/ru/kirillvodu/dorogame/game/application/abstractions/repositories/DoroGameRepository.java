package ru.kirillvodu.dorogame.game.application.abstractions.repositories;

import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoroGameRepository {
    List<DoroGame> getAll();
    List<DoroGame> getByUserIdAndFinishedTrue(UUID userId);
    Optional<DoroGame> getById(UUID id);
    DoroGame save(DoroGame game);
    void deleteById(UUID id);
}
