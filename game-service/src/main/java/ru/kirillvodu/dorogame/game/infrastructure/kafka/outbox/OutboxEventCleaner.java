package ru.kirillvodu.dorogame.game.infrastructure.kafka.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.OutboxEventEntityRepository;

import java.time.Duration;
import java.time.Instant;

@Component
public class OutboxEventCleaner {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventCleaner.class);
    private static final Duration RETENTION = Duration.ofDays(1);

    private final OutboxEventEntityRepository repository;

    public OutboxEventCleaner(OutboxEventEntityRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupProcessedEvents() {
        Instant threshold = Instant.now().minus(RETENTION);
        repository.deleteAllByCreatedAtBefore(threshold);
        log.info("Cleaned up outbox events older than {}", threshold);
    }
}
