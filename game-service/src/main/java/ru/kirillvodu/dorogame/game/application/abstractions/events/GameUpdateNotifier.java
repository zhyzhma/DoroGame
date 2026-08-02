package ru.kirillvodu.dorogame.game.application.abstractions.events;

import ru.kirillvodu.dorogame.game.domain.model.DoroGame;

public interface GameUpdateNotifier {
    void notifyGameUpdated(DoroGame game);
}
