package ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.kirillvodu.dorogame.stats.domain.model.PlayerStats;

import java.util.UUID;

@Entity
@Table(name = "player_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStatsEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "wins", nullable = false)
    private int wins;

    @Column(name = "losses", nullable = false)
    private int losses;

    @Column(name = "rating", nullable = false)
    private int rating;

    public PlayerStats toDomain() {
        return new PlayerStats(userId, wins, losses, rating);
    }

    public static PlayerStatsEntity fromDomain(PlayerStats stats) {
        PlayerStatsEntity entity = PlayerStatsEntity.builder()
                .userId(stats.getUserId())
                .wins(stats.getWins())
                .losses(stats.getLosses())
                .rating(stats.getRating())
                .build();
        return entity;
    }
}
