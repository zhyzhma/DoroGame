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
import ru.kirillvodu.dorogame.game.domain.model.game.field.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipMoveEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.DoroGameEntity;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

class GameRecordsControllerTest extends IntegrationTestBase {

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

    private DoroGameEntity persistFinishedGame() {
        ChipEntity chip1 = ChipEntity.builder()
                .id(UUID.randomUUID()).coordX(0).coordY(0).playerNumber(1).build();
        ChipEntity chip2 = ChipEntity.builder()
                .id(UUID.randomUUID()).coordX(8).coordY(0).playerNumber(2).build();

        DoroGameEntity entity = DoroGameEntity.builder()
                .player1Id(player1Id)
                .player1Name("Player1")
                .player2Id(player2Id)
                .player2Name("Player2")
                .fieldVariant(FieldVariant.STANDARD)
                .winCheckerVariant(WinCheckerVariant.STANDARD)
                .finished(true)
                .winner(1)
                .turn(1)
                .moveCounter(1)
                .startPositions(List.of(chip1, chip2))
                .build();
        entity.setId(UUID.randomUUID());
        DoroGameEntity saved = doroGameRepository.save(entity);

        ChipEntity chipRef = new ChipEntity();
        chipRef.setId(chip1.getId());
        ChipMoveEntity move = new ChipMoveEntity();
        move.setId(UUID.randomUUID());
        move.setChipEntity(chipRef);
        move.setMoveIdx(0);
        move.setXAfter(1);
        move.setYAfter(0);
        chipMoveEntityRepository.save(move);

        return saved;
    }

    @Test
    void getGameRecord_returnsRecord_whenGameFinishedAndUserIsParticipant() throws Exception {
        // Arrange
        DoroGameEntity game = persistFinishedGame();

        // Act & Assert
        mockMvc.perform(authed(get("/records/" + game.getId()))
                        .header("X-User-Id", player1Id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(game.getId().toString()))
                .andExpect(jsonPath("$.player1Id").value(player1Id.toString()))
                .andExpect(jsonPath("$.winner").value(1))
                .andExpect(jsonPath("$.chipMoves.length()").value(1))
                .andExpect(jsonPath("$.startPositions1.length()").value(1))
                .andExpect(jsonPath("$.startPositions2.length()").value(1));
    }

    @Test
    void getGameRecord_returnsRecord_whenRequestedByPlayer2() throws Exception {
        // Arrange
        DoroGameEntity game = persistFinishedGame();

        // Act & Assert
        mockMvc.perform(authed(get("/records/" + game.getId()))
                        .header("X-User-Id", player2Id.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void getGameRecord_returns404_whenGameNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(authed(get("/records/" + UUID.randomUUID()))
                        .header("X-User-Id", player1Id.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGameRecord_returns404_whenUserNotParticipant() throws Exception {
        // Arrange
        DoroGameEntity game = persistFinishedGame();

        // Act & Assert
        mockMvc.perform(authed(get("/records/" + game.getId()))
                        .header("X-User-Id", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGameRecord_returns409_whenGameNotFinished() throws Exception {
        // Arrange
        ChipEntity chip1 = ChipEntity.builder()
                .id(UUID.randomUUID()).coordX(0).coordY(0).playerNumber(1).build();
        ChipEntity chip2 = ChipEntity.builder()
                .id(UUID.randomUUID()).coordX(8).coordY(0).playerNumber(2).build();
        DoroGameEntity entity = DoroGameEntity.builder()
                .player1Id(player1Id)
                .player1Name("Player1")
                .player2Id(player2Id)
                .player2Name("Player2")
                .fieldVariant(FieldVariant.STANDARD)
                .winCheckerVariant(WinCheckerVariant.STANDARD)
                .finished(false)
                .winner(0)
                .turn(1)
                .moveCounter(0)
                .startPositions(List.of(chip1, chip2))
                .build();
        entity.setId(UUID.randomUUID());
        DoroGameEntity saved = doroGameRepository.save(entity);

        // Act & Assert
        mockMvc.perform(authed(get("/records/" + saved.getId()))
                        .header("X-User-Id", player1Id.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    void getGameRecord_returns401_whenTokenMissing() throws Exception {
        // Arrange
        DoroGameEntity game = persistFinishedGame();

        // Act & Assert
        mockMvc.perform(get("/records/" + game.getId())
                        .header("X-User-Id", player1Id.toString()))
                .andExpect(status().isUnauthorized());
    }
}
