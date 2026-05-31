package ru.kirillvodu.dorogame.stats.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayerStats {
    private UUID userId;
    private int wins;
    private int losses;
    private int rating;

    public PlayerStats(UUID userId, int wins, int losses, int rating) {
        this.userId = userId;
        this.wins = wins;
        this.losses = losses;
        this.rating = rating;
    }

    public static PlayerStats initial(UUID userId) {
        return new PlayerStats(userId, 0, 0, 1000);
    }

    public void recordWin() {
        wins++;
        rating += 25;
    }

    public void recordLoss() {
        losses++;
        rating = Math.max(0, rating - 25);
    }
}
