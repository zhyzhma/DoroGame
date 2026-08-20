package ru.kirillvodu.dorogame.user.infrastructure.persistence.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.user.domain.model.User;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.entities.UserEntity;

@Component
public class UserEntityMapper {

    public User toDomain(UserEntity entity) {
        return new User(entity.getKeycloakId(), entity.getName(), entity.getAvatarKey());
    }

    public UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .keycloakId(user.getId())
                .name(user.getName())
                .avatarKey(user.getAvatarKey())
                .build();
    }
}
