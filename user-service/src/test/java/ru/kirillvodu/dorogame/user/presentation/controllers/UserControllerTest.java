package ru.kirillvodu.dorogame.user.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kirillvodu.dorogame.user.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTestBase {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void register_createsUserAndReturnsDto_whenRequestValid() throws Exception {
        // Arrange
        String body = """
                {"name": "Alice", "password": "secret"}
                """;

        // Act & Assert
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.id").isNotEmpty());

        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test
    void getById_returnsUser_whenUserFound() throws Exception {
        // Arrange
        String body = """
                {"name": "Bob", "password": "secret"}
                """;
        String response = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        // Act & Assert
        mockMvc.perform(authed(get("/users/{id}", id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getById_returns404_whenUserNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(authed(get("/users/{id}", "00000000-0000-0000-0000-000000000000")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void getById_returns401_whenTokenMissing() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/users/{id}", "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isUnauthorized());
    }
}
