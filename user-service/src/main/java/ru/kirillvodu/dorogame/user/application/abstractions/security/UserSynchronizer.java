package ru.kirillvodu.dorogame.user.application.abstractions.security;

import org.springframework.security.oauth2.jwt.Jwt;

public interface UserSynchronizer {
    void synchronize(Jwt jwt);
}
