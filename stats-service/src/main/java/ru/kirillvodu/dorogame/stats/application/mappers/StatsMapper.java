package ru.kirillvodu.dorogame.stats.application.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.stats.application.contracts.DTO.read.GameResultDTO;
import ru.kirillvodu.dorogame.stats.application.contracts.DTO.read.PlayerStatsDTO;
import ru.kirillvodu.dorogame.stats.domain.model.GameResult;
import ru.kirillvodu.dorogame.stats.domain.model.PlayerStats;

@Component
public class StatsMapper {

    public PlayerStatsDTO toDto(PlayerStats stats) {
        return new PlayerStatsDTO(stats.getUserId(), stats.getWins(), stats.getLosses(), stats.getRating());
    }

    public GameResultDTO toDto(GameResult result) {
        return new GameResultDTO(result.getId(), result.getGameId(), result.getWinnerId(),
                result.getLoserId(), result.getFinishedAt());
    }
}
