package ru.kirillvodu.dorogame.game.presentation.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.kirillvodu.dorogame.game.IntegrationTestBase;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PublicInvitationControllerTest extends IntegrationTestBase {

    @Test
    void createPublicInvitation_returnsCreatedInvitation() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userServiceAbstraction.getById(userId))
                .thenReturn(new UserReadModel(userId, "TestPlayer"));

        String body = """
                {
                  "fieldVariant": "STANDARD",
                  "winCheckerVariant": "STANDARD",
                  "turn": 1
                }
                """;

        mockMvc.perform(post("/invitations/public")
                        .header("X-User-Id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.fieldVariant").value("STANDARD"));

        assertThat(invitationRepository.findAll()).hasSize(1);
    }

    @Test
    void getAllPublicInvitations_returnsEmptyList_whenNoneExist() throws Exception {
        mockMvc.perform(get("/invitations/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllPublicInvitations_returnsInvitations_whenExist() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userServiceAbstraction.getById(userId))
                .thenReturn(new UserReadModel(userId, "Player"));

        String body = """
                {"fieldVariant":"STANDARD","winCheckerVariant":"STANDARD","turn":1}
                """;
        mockMvc.perform(post("/invitations/public")
                        .header("X-User-Id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body));

        mockMvc.perform(get("/invitations/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
