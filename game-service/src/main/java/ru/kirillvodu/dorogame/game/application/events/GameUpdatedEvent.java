package ru.kirillvodu.dorogame.game.application.events;

import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;

public record GameUpdatedEvent(DoroGame game) {
}
