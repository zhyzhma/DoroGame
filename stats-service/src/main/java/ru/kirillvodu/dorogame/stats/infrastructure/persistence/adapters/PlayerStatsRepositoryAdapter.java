package ru.kirillvodu.dorogame.stats.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.stats.application.abstractions.repositories.PlayerStatsRepository;
import ru.kirillvodu.dorogame.stats.domain.model.PlayerStats;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.mappers.PlayerStatsEntityMapper;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories.PlayerStatsEntityRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PlayerStatsRepositoryAdapter implements PlayerStatsRepository {

    private final PlayerStatsEntityRepository repository;
    private final PlayerStatsEntityMapper mapper;

    public PlayerStatsRepositoryAdapter(PlayerStatsEntityRepository repository, PlayerStatsEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PlayerStats save(PlayerStats stats) {
        return mapper.toDomain(repository.save(mapper.fromDomain(stats)));
    }

    @Override
    public Optional<PlayerStats> getByUserId(UUID userId) {
        return repository.findByUserId(userId).map(mapper::toDomain);
    }
}
