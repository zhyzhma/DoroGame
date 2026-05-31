package ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.kirillvodu.dorogame.stats.domain.model.GameResult;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResultEntity extends BaseEntity {

    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column(name = "winner_id", nullable = false)
    private UUID winnerId;

    @Column(name = "loser_id", nullable = false)
    private UUID loserId;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

    public GameResult toDomain() {
        return new GameResult(getId(), gameId, winnerId, loserId, finishedAt);
    }

    public static GameResultEntity fromDomain(GameResult result) {
        GameResultEntity entity = GameResultEntity.builder()
                .gameId(result.getGameId())
                .winnerId(result.getWinnerId())
                .loserId(result.getLoserId())
                .finishedAt(result.getFinishedAt())
                .build();
        entity.setId(result.getId());
        return entity;
    }
}
