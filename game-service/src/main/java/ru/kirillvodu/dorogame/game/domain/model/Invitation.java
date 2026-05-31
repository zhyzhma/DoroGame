package ru.kirillvodu.dorogame.game.domain.model;

import java.util.UUID;

public record Invitation(UUID id, UserReadModel user, UUID targetUserId, GameConfig gameConfig) {

    public static Invitation createPublic(UserReadModel user, GameConfig gameConfig) {
        return new Invitation(null, user, null, gameConfig);
    }

    public static Invitation createPrivate(UserReadModel user, UUID targetUserId, GameConfig gameConfig) {
        return new Invitation(null, user, targetUserId, gameConfig);
    }

    public boolean isPrivate() {
        return targetUserId != null;
    }
}
