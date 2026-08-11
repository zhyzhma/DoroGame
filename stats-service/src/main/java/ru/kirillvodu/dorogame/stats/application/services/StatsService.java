package ru.kirillvodu.dorogame.stats.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillvodu.dorogame.stats.application.abstractions.repositories.GameResultRepository;
import ru.kirillvodu.dorogame.stats.application.abstractions.repositories.PlayerStatsRepository;
import ru.kirillvodu.dorogame.stats.application.exceptions.DuplicateGameResultException;
import ru.kirillvodu.dorogame.stats.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.stats.domain.model.GameResult;
import ru.kirillvodu.dorogame.stats.domain.model.PlayerStats;

import java.util.List;
import java.util.UUID;

@Service
public class StatsService {

    private static final Logger log = LoggerFactory.getLogger(StatsService.class);

    @Autowired
    private GameResultRepository gameResultRepository;
    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Transactional
    public void recordGameResult(UUID gameId, UUID winnerId, UUID loserId) {
        GameResult result = GameResult.create(gameId, winnerId, loserId);
        try {
            gameResultRepository.save(result);
        } catch (DuplicateGameResultException e) {
            log.info("Skipping already recorded game result: {}", gameId);
            return;
        }

        PlayerStats winnerStats = playerStatsRepository.getByUserId(winnerId)
                .orElse(PlayerStats.initial(winnerId));
        winnerStats.recordWin();
        playerStatsRepository.save(winnerStats);

        PlayerStats loserStats = playerStatsRepository.getByUserId(loserId)
                .orElse(PlayerStats.initial(loserId));
        loserStats.recordLoss();
        playerStatsRepository.save(loserStats);
    }

    public PlayerStats getPlayerStats(UUID userId) {
        return playerStatsRepository.getByUserId(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId, "PlayerStats"));
    }

    public List<GameResult> getGameHistory(UUID userId) {
        return gameResultRepository.getByUserId(userId);
    }
}
