package ru.kirillvodu.dorogame.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.kirillvodu.dorogame.game.application.abstractions.services.UserServiceAbstraction;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.DoroGameEntityRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.InvitationEntityRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.OutboxEventEntityRepository;

@SpringBootTest(classes = GameServiceApp.class)
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

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

    @Autowired protected DoroGameEntityRepository doroGameRepository;
    @Autowired protected InvitationEntityRepository invitationRepository;
    @Autowired protected OutboxEventEntityRepository outboxRepository;

    @BeforeEach
    void cleanDatabase() {
        outboxRepository.deleteAll();
        doroGameRepository.deleteAll();
        invitationRepository.deleteAll();
    }
}
