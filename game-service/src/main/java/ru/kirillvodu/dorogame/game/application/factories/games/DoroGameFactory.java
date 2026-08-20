package ru.kirillvodu.dorogame.game.application.factories.games;

import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.invitation.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;

public interface DoroGameFactory {
    DoroGame createDoroGame(UserReadModel acceptingUser, Invitation invitation);
}
