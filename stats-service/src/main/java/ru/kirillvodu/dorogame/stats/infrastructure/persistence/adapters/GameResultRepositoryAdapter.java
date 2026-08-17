package ru.kirillvodu.dorogame.stats.infrastructure.persistence.adapters;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.stats.application.abstractions.repositories.GameResultRepository;
import ru.kirillvodu.dorogame.stats.application.exceptions.DuplicateGameResultException;
import ru.kirillvodu.dorogame.stats.domain.model.GameResult;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.mappers.GameResultEntityMapper;
import ru.kirillvodu.dorogame.stats.infrastructure.persistence.repositories.GameResultEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GameResultRepositoryAdapter implements GameResultRepository {

    private final GameResultEntityRepository repository;
    private final GameResultEntityMapper mapper;

    public GameResultRepositoryAdapter(GameResultEntityRepository repository, GameResultEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GameResult save(GameResult result) {
        try {
            return mapper.toDomain(repository.saveAndFlush(mapper.fromDomain(result)));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateGameResultException(result.getGameId());
        }
    }

    @Override
    public Optional<GameResult> getById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<GameResult> getByUserId(UUID userId) {
        return repository.findAllByWinnerIdOrLoserId(userId, userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
