package ru.kirillvodu.dorogame.game.presentation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.kirillvodu.dorogame.game.IntegrationTestBase;
import ru.kirillvodu.dorogame.game.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.game.application.abstractions.services.UserServiceAbstraction;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PublicInvitationControllerTest extends IntegrationTestBase {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private UserServiceAbstraction userServiceAbstraction;
    @MockitoBean private CurrentUserProvider currentUserProvider;

    @BeforeEach
    void setupCurrentUser() {
        when(currentUserProvider.getCurrentUserId()).thenAnswer(invocation -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return UUID.fromString(attrs.getRequest().getHeader("X-User-Id"));
        });
    }

    @Test
    void createPublicInvitation_returnsCreatedInvitation_whenRequestValid() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(authed(post("/invitations/public"))
                        .header("X-User-Id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.fieldVariant").value("STANDARD"));

        assertThat(invitationRepository.findAll()).hasSize(1);
    }

    @Test
    void getAllPublicInvitations_returnsEmptyList_whenNoInvitationsExist() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(authed(get("/invitations/public")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllPublicInvitations_returnsInvitations_whenInvitationsExist() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userServiceAbstraction.getById(userId))
                .thenReturn(new UserReadModel(userId, "Player"));

        String body = """
                {"fieldVariant":"STANDARD","winCheckerVariant":"STANDARD","turn":1}
                """;
        mockMvc.perform(authed(post("/invitations/public"))
                        .header("X-User-Id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body));

        // Act & Assert
        mockMvc.perform(authed(get("/invitations/public")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void createPublicInvitation_returns401_whenTokenMissing() throws Exception {
        // Arrange
        String body = """
                {"fieldVariant":"STANDARD","winCheckerVariant":"STANDARD","turn":1}
                """;

        // Act & Assert
        mockMvc.perform(post("/invitations/public")
                        .header("X-User-Id", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }
}
