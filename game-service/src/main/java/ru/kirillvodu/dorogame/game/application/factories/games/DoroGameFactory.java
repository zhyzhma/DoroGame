package ru.kirillvodu.dorogame.game.application.factories.games;

import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;

public interface DoroGameFactory {
    DoroGame createDoroGame(UserReadModel acceptingUser, Invitation invitation);
}
