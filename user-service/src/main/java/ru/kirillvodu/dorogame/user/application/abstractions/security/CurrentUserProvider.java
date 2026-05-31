package ru.kirillvodu.dorogame.user.application.abstractions.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUserId();
}
