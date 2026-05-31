package ru.kirillvodu.dorogame.game.infrastructure.kafka.outbox;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.OutboxEventEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.OutboxEventEntityRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventPublisher.class);

    private final OutboxEventEntityRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    public void publishPendingEvents() {
        List<OutboxEventEntity> pending = repository.findAllBySentFalse();

        for (OutboxEventEntity event : pending) {
            try {
                kafkaTemplate.send(event.getEventType(), event.getAggregateId(), event.getPayload());
                event.setSent(true);
                repository.save(event);
            } catch (Exception e) {
                log.error("Failed to publish outbox event id={} type={}: {}",
                        event.getId(), event.getEventType(), e.getMessage());
            }
        }
    }
}
