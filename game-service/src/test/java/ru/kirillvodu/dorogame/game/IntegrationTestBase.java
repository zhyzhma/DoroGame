package ru.kirillvodu.dorogame.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.kirillvodu.dorogame.game.application.abstractions.security.CurrentUserProvider;
import ru.kirillvodu.dorogame.game.application.abstractions.services.UserServiceAbstraction;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.DoroGameEntityRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.InvitationEntityRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.OutboxEventEntityRepository;

import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = GameServiceApp.class)
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
            .withDatabaseName("gamedb_test")
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
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9999");
        registry.add("spring.autoconfigure.exclude",
                () -> "org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration");
    }

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

    @MockBean protected KafkaTemplate<String, String> kafkaTemplate;
    @MockBean protected UserServiceAbstraction userServiceAbstraction;
    @MockBean protected CurrentUserProvider currentUserProvider;

    @Autowired protected DoroGameEntityRepository doroGameRepository;
    @Autowired protected InvitationEntityRepository invitationRepository;
    @Autowired protected OutboxEventEntityRepository outboxRepository;

    @BeforeEach
    void cleanDatabase() {
        outboxRepository.deleteAll();
        doroGameRepository.deleteAll();
        invitationRepository.deleteAll();
    }

    @BeforeEach
    void setupCurrentUser() {
        when(currentUserProvider.getCurrentUserId()).thenAnswer(invocation -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return UUID.fromString(attrs.getRequest().getHeader("X-User-Id"));
        });
    }
}
