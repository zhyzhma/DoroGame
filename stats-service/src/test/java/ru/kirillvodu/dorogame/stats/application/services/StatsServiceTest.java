package ru.kirillvodu.dorogame.stats.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kirillvodu.dorogame.stats.application.abstractions.repositories.GameResultRepository;
import ru.kirillvodu.dorogame.stats.application.abstractions.repositories.PlayerStatsRepository;
import ru.kirillvodu.dorogame.stats.application.exceptions.DuplicateGameResultException;
import ru.kirillvodu.dorogame.stats.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.stats.domain.model.GameResult;
import ru.kirillvodu.dorogame.stats.domain.model.PlayerStats;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock private GameResultRepository gameResultRepository;
    @Mock private PlayerStatsRepository playerStatsRepository;

    @InjectMocks private StatsService statsService;

    @Test
    void recordGameResult_createsInitialStatsAndRecordsResult_whenPlayersNew() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();

        when(gameResultRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(playerStatsRepository.getByUserId(winnerId)).thenReturn(Optional.empty());
        when(playerStatsRepository.getByUserId(loserId)).thenReturn(Optional.empty());
        when(playerStatsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        statsService.recordGameResult(gameId, winnerId, loserId);

        // Assert
        ArgumentCaptor<PlayerStats> captor = ArgumentCaptor.forClass(PlayerStats.class);
        verify(playerStatsRepository, times(2)).save(captor.capture());

        List<PlayerStats> saved = captor.getAllValues();
        PlayerStats winner = saved.stream().filter(s -> s.getUserId().equals(winnerId)).findFirst().orElseThrow();
        PlayerStats loser = saved.stream().filter(s -> s.getUserId().equals(loserId)).findFirst().orElseThrow();

        assertThat(winner.getWins()).isEqualTo(1);
        assertThat(winner.getRating()).isEqualTo(1025);
        assertThat(loser.getLosses()).isEqualTo(1);
        assertThat(loser.getRating()).isEqualTo(975);
    }

    @Test
    void recordGameResult_skipsStatsUpdate_whenGameAlreadyRecorded() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();
        when(gameResultRepository.save(any())).thenThrow(new DuplicateGameResultException(gameId));

        // Act
        statsService.recordGameResult(gameId, winnerId, loserId);

        // Assert
        verifyNoInteractions(playerStatsRepository);
    }

    @Test
    void recordGameResult_updatesStats_whenPlayersExisting() {
        // Arrange
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();
        PlayerStats existingWinner = new PlayerStats(winnerId, 3, 1, 1075);
        PlayerStats existingLoser = new PlayerStats(loserId, 1, 2, 950);

        when(gameResultRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(playerStatsRepository.getByUserId(winnerId)).thenReturn(Optional.of(existingWinner));
        when(playerStatsRepository.getByUserId(loserId)).thenReturn(Optional.of(existingLoser));
        when(playerStatsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        statsService.recordGameResult(UUID.randomUUID(), winnerId, loserId);

        // Assert
        assertThat(existingWinner.getWins()).isEqualTo(4);
        assertThat(existingWinner.getRating()).isEqualTo(1100);
        assertThat(existingLoser.getLosses()).isEqualTo(3);
        assertThat(existingLoser.getRating()).isEqualTo(925);
    }

    @Test
    void getPlayerStats_returnsStats_whenStatsFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        PlayerStats stats = new PlayerStats(userId, 5, 2, 1125);
        when(playerStatsRepository.getByUserId(userId)).thenReturn(Optional.of(stats));

        // Act
        PlayerStats result = statsService.getPlayerStats(userId);

        // Assert
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getWins()).isEqualTo(5);
    }

    @Test
    void getPlayerStats_throwsObjectNotFoundException_whenStatsNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(playerStatsRepository.getByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> statsService.getPlayerStats(userId));
    }

    @Test
    void getGameHistory_returnsResultsForUser_whenResultsExist() {
        // Arrange
        UUID userId = UUID.randomUUID();
        List<GameResult> results = List.of(
                GameResult.create(UUID.randomUUID(), userId, UUID.randomUUID()),
                GameResult.create(UUID.randomUUID(), UUID.randomUUID(), userId)
        );
        when(gameResultRepository.getByUserId(userId)).thenReturn(results);

        // Act
        List<GameResult> result = statsService.getGameHistory(userId);

        // Assert
        assertThat(result).hasSize(2);
    }
}
