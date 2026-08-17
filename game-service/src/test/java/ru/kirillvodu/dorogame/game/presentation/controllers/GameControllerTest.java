package ru.kirillvodu.dorogame.game.presentation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.kirillvodu.dorogame.game.IntegrationTestBase;
import ru.kirillvodu.dorogame.game.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.DoroGameEntity;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GameControllerTest extends IntegrationTestBase {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private CurrentUserProvider currentUserProvider;

    private UUID player1Id;
    private UUID player2Id;

    @BeforeEach
    void setupCurrentUser() {
        when(currentUserProvider.getCurrentUserId()).thenAnswer(invocation -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return UUID.fromString(attrs.getRequest().getHeader("X-User-Id"));
        });
        player1Id = UUID.randomUUID();
        player2Id = UUID.randomUUID();
    }

    private DoroGameEntity persistGame(UUID p1, UUID p2, boolean finished) {
        ChipEntity chip1 = ChipEntity.builder()
                .id(UUID.randomUUID()).coordX(0).coordY(0).playerNumber(1).build();
        ChipEntity chip2 = ChipEntity.builder()
                .id(UUID.randomUUID()).coordX(8).coordY(0).playerNumber(2).build();
        DoroGameEntity entity = DoroGameEntity.builder()
                .player1Id(p1)
                .player1Name("Player1")
                .player2Id(p2)
                .player2Name("Player2")
                .fieldVariant(FieldVariant.STANDARD)
                .winCheckerVariant(WinCheckerVariant.STANDARD)
                .finished(finished)
                .winner(finished ? 1 : 0)
                .turn(1)
                .moveCounter(0)
                .startPositions(List.of(chip1, chip2))
                .build();
        entity.setId(UUID.randomUUID());
        return doroGameRepository.save(entity);
    }

    @Test
    void getFinishedGames_returnsOnlyFinishedGamesOfCurrentUser() throws Exception {
        // Arrange
        DoroGameEntity finishedGame = persistGame(player1Id, player2Id, true);
        persistGame(player1Id, player2Id, false);
        persistGame(UUID.randomUUID(), UUID.randomUUID(), true);

        // Act & Assert
        mockMvc.perform(authed(get("/games/finished"))
                        .header("X-User-Id", player1Id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(finishedGame.getId().toString()));
    }

    @Test
    void getFinishedGames_returnsEmptyList_whenNoFinishedGames() throws Exception {
        // Act & Assert
        mockMvc.perform(authed(get("/games/finished"))
                        .header("X-User-Id", player1Id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getFinishedGames_includesGamesWhereUserIsPlayer2() throws Exception {
        // Arrange
        persistGame(player2Id, player1Id, true);

        // Act & Assert
        mockMvc.perform(authed(get("/games/finished"))
                        .header("X-User-Id", player1Id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getFinishedGames_returns401_whenTokenMissing() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/games/finished")
                        .header("X-User-Id", player1Id.toString()))
                .andExpect(status().isUnauthorized());
    }
}
