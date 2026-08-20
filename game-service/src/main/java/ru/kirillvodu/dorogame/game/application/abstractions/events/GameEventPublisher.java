package ru.kirillvodu.dorogame.game.application.abstractions.events;

import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;

public interface GameEventPublisher {
    void publishGameFinished(DoroGame game);
}
