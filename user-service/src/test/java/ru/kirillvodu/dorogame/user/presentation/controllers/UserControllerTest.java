package ru.kirillvodu.dorogame.user.presentation.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.kirillvodu.dorogame.user.IntegrationTestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTestBase {

    @Test
    void register_createsUserAndReturnsDto() throws Exception {
        String body = """
                {"name": "Alice", "password": "secret"}
                """;

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
    void getById_found_returnsUser() throws Exception {
        String body = """
                {"name": "Bob", "password": "secret"}
                """;
        String response = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/users/{id}", "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }
}
