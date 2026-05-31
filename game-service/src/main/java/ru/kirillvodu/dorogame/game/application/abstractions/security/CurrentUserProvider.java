package ru.kirillvodu.dorogame.game.application.abstractions.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUserId();
}
