package ru.kirillvodu.dorogame.user.infrastructure.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.user.application.abstractions.security.UserSynchronizer;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.entities.UserEntity;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.repositories.UserEntityRepository;

import java.util.UUID;

@Component
public class KeycloakUserSynchronizer implements UserSynchronizer {

    private final UserEntityRepository userEntityRepository;

    public KeycloakUserSynchronizer(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public void synchronize(Jwt jwt) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        String name = jwt.getClaimAsString("preferred_username");
        userEntityRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> userEntityRepository.save(
                        UserEntity.builder()
                                .keycloakId(keycloakId)
                                .name(name != null ? name : "Player")
                                .score(0)
                                .build()));
    }
}
