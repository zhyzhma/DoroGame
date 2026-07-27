package ru.kirillvodu.dorogame.stats;

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
import org.testcontainers.containers.PostgreSQLContainer;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories.GameResultEntityRepository;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories.PlayerStatsEntityRepository;

@SpringBootTest(classes = StatsServiceApp.class)
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
            .withDatabaseName("statsdb_test")
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

    @Autowired protected GameResultEntityRepository gameResultRepository;
    @Autowired protected PlayerStatsEntityRepository playerStatsRepository;

    @BeforeEach
    void cleanDatabase() {
        gameResultRepository.deleteAll();
        playerStatsRepository.deleteAll();
    }
}
