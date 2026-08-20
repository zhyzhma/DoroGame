package ru.kirillvodu.dorogame.user.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    @Setter
    private String name;
    @Setter
    private String avatarKey;

    public User(UUID id, String name, String avatarKey) {
        this.id = id;
        this.name = name;
        this.avatarKey = avatarKey;
    }

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.avatarKey = null;
    }
}
