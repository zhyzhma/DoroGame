package ru.kirillvodu.dorogame.game.infrastructure.security;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.abstractions.security.CurrentUserProvider;

import java.util.UUID;

@Primary
@Component
public class KeycloakCurrentUserProvider implements CurrentUserProvider {

    @Override
    public UUID getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UUID.fromString(jwt.getSubject());
    }
}
