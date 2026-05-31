package ru.kirillvodu.dorogame.stats.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.stats.application.abstractions.repositories.GameResultRepository;
import ru.kirillvodu.dorogame.stats.domain.model.GameResult;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.entities.GameResultEntity;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories.GameResultEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GameResultRepositoryAdapter implements GameResultRepository {

    private final GameResultEntityRepository repository;

    public GameResultRepositoryAdapter(GameResultEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public GameResult save(GameResult result) {
        return repository.save(GameResultEntity.fromDomain(result)).toDomain();
    }

    @Override
    public Optional<GameResult> getById(UUID id) {
        return repository.findById(id).map(GameResultEntity::toDomain);
    }

    @Override
    public List<GameResult> getByUserId(UUID userId) {
        return repository.findAllByWinnerIdOrLoserId(userId, userId).stream()
                .map(GameResultEntity::toDomain)
                .toList();
    }
}
