package ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Id
    private UUID id;

    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column(name = "winner_id", nullable = false)
    private UUID winnerId;

    @Column(name = "loser_id", nullable = false)
    private UUID loserId;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;
}
