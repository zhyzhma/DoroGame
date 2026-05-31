package ru.kirillvodu.dorogame.user.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.user.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.user.application.abstractions.security.UserSynchronizer;

import java.util.UUID;

@Component
public class KeycloakCurrentUserProvider implements CurrentUserProvider {

    private final UserSynchronizer userSynchronizer;

    public KeycloakCurrentUserProvider(UserSynchronizer userSynchronizer) {
        this.userSynchronizer = userSynchronizer;
    }

    @Override
    public UUID getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userSynchronizer.synchronize(jwt);
        return UUID.fromString(jwt.getSubject());
    }
}
