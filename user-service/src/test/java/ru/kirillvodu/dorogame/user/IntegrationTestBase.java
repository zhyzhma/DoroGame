package ru.kirillvodu.dorogame.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.kirillvodu.dorogame.user.application.abstractions.identity.IdentityProvider;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.repositories.UserEntityRepository;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserServiceApp.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(IntegrationTestBase.TestSecurityConfig.class)
public abstract class IntegrationTestBase {

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public JwtDecoder jwtDecoder() {
            return token -> {
                throw new UnsupportedOperationException("JWT decoding disabled in tests");
            };
        }
    }

    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("userdb_test")
            .withUsername("user")
            .withPassword("password");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("grpc.server.port", () -> "0");
    }

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected UserEntityRepository userRepository;

    @MockBean protected IdentityProvider identityProvider;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void stubIdentityProvider() {
        when(identityProvider.createUser(anyString(), anyString()))
                .thenAnswer(invocation -> UUID.randomUUID().toString());
    }
}
