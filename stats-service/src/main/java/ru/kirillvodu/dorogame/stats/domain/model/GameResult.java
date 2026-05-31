package ru.kirillvodu.dorogame.stats.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class GameResult {
    private UUID id;
    private UUID gameId;
    private UUID winnerId;
    private UUID loserId;
    private LocalDateTime finishedAt;

    public GameResult(UUID id, UUID gameId, UUID winnerId, UUID loserId, LocalDateTime finishedAt) {
        this.id = id;
        this.gameId = gameId;
        this.winnerId = winnerId;
        this.loserId = loserId;
        this.finishedAt = finishedAt;
    }

    public static GameResult create(UUID gameId, UUID winnerId, UUID loserId) {
        return new GameResult(null, gameId, winnerId, loserId, LocalDateTime.now());
    }
}
