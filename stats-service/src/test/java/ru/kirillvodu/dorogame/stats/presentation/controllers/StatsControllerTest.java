package ru.kirillvodu.dorogame.stats.presentation.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ru.kirillvodu.dorogame.stats.IntegrationTestBase;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities.GameResultEntity;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities.PlayerStatsEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StatsControllerTest extends IntegrationTestBase {

    @Autowired private MockMvc mockMvc;

    @Test
    void getStats_returnsPlayerStats_whenStatsFound() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        PlayerStatsEntity entity = PlayerStatsEntity.builder()
                .userId(userId)
                .wins(5)
                .losses(2)
                .rating(1125)
                .build();
        playerStatsRepository.save(entity);

        // Act & Assert
        mockMvc.perform(authed(get("/stats/{userId}", userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.wins").value(5))
                .andExpect(jsonPath("$.losses").value(2))
                .andExpect(jsonPath("$.rating").value(1125));
    }

    @Test
    void getStats_returns404_whenStatsNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(authed(get("/stats/{userId}", UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHistory_returnsGameResults_whenResultsExist() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        GameResultEntity result = GameResultEntity.builder()
                .gameId(UUID.randomUUID())
                .winnerId(userId)
                .loserId(UUID.randomUUID())
                .finishedAt(LocalDateTime.now())
                .build();
        gameResultRepository.save(result);

        // Act & Assert
        mockMvc.perform(authed(get("/stats/{userId}/history", userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].winnerId").value(userId.toString()));
    }

    @Test
    void getHistory_returnsEmptyList_whenNoResultsExist() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(authed(get("/stats/{userId}/history", UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getStats_returns401_whenTokenMissing() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/stats/{userId}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }
}
