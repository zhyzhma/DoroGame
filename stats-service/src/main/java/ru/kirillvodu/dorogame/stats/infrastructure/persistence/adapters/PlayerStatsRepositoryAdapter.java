package ru.kirillvodu.dorogame.stats.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.stats.application.abstractions.repositories.PlayerStatsRepository;
import ru.kirillvodu.dorogame.stats.domain.model.PlayerStats;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities.PlayerStatsEntity;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories.PlayerStatsEntityRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PlayerStatsRepositoryAdapter implements PlayerStatsRepository {

    private final PlayerStatsEntityRepository repository;

    public PlayerStatsRepositoryAdapter(PlayerStatsEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public PlayerStats save(PlayerStats stats) {
        return repository.save(PlayerStatsEntity.fromDomain(stats)).toDomain();
    }

    @Override
    public Optional<PlayerStats> getByUserId(UUID userId) {
        return repository.findByUserId(userId).map(PlayerStatsEntity::toDomain);
    }
}
