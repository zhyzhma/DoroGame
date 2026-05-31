package ru.kirillvodu.dorogame.stats.application.contracts.DTO.read;

import java.util.UUID;

public record PlayerStatsDTO(UUID userId, int wins, int losses, int rating) {
}
