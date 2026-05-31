package ru.kirillvodu.dorogame.user.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    @Setter
    private String name;
    @Setter
    private int score;

    public User(UUID id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public User(String name, int score) {
        this.id = null;
        this.name = name;
        this.score = score;
    }
}
