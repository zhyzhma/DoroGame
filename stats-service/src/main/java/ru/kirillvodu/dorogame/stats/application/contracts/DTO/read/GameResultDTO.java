package ru.kirillvodu.dorogame.stats.application.contracts.DTO.read;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameResultDTO(UUID id, UUID gameId, UUID winnerId, UUID loserId, LocalDateTime finishedAt) {
}
