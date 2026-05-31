package ru.kirillvodu.dorogame.stats.presentation.controllers;

import org.springframework.web.bind.annotation.*;
import ru.kirillvodu.dorogame.stats.application.contracts.DTO.read.GameResultDTO;
import ru.kirillvodu.dorogame.stats.application.contracts.DTO.read.PlayerStatsDTO;
import ru.kirillvodu.dorogame.stats.application.mappers.StatsMapper;
import ru.kirillvodu.dorogame.stats.application.services.StatsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;
    private final StatsMapper statsMapper;

    public StatsController(StatsService statsService, StatsMapper statsMapper) {
        this.statsService = statsService;
        this.statsMapper = statsMapper;
    }

    @GetMapping("/{userId}")
    public PlayerStatsDTO getStats(@PathVariable UUID userId) {
        return statsMapper.toDto(statsService.getPlayerStats(userId));
    }

    @GetMapping("/{userId}/history")
    public List<GameResultDTO> getHistory(@PathVariable UUID userId) {
        return statsService.getGameHistory(userId).stream()
                .map(statsMapper::toDto)
                .toList();
    }
}
