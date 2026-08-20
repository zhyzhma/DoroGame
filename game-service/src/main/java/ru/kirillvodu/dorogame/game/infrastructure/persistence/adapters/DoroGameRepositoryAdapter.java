package ru.kirillvodu.dorogame.game.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.factories.fields.FieldFactory;
import ru.kirillvodu.dorogame.game.application.factories.winCheckers.WinCheckerFactory;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.game.field.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.field.Field;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinChecker;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipMoveEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.DoroGameEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.mappers.DoroGameEntityMapper;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.ChipMoveEntityRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.DoroGameEntityRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DoroGameRepositoryAdapter implements DoroGameRepository {

    private final DoroGameEntityRepository repository;
    private final Map<UUID, DoroGame> doroGameCache = new HashMap<>();
    private final ChipMoveEntityRepository chipMoveEntityRepository;
    private final DoroGameEntityMapper mapper;
    private final Map<FieldVariant, FieldFactory> fieldFactoryMap;
    private final Map<WinCheckerVariant, WinCheckerFactory> winCheckerFactoryMap;
    private final int movesBatchSize = 5;

    public DoroGameRepositoryAdapter(DoroGameEntityRepository repository,
                                     ChipMoveEntityRepository chipMoveEntityRepository,
                                     DoroGameEntityMapper mapper,
                                     List<FieldFactory> fieldFactories,
                                     List<WinCheckerFactory> winCheckerFactories) {
        this.repository = repository;
        this.chipMoveEntityRepository = chipMoveEntityRepository;
        this.mapper = mapper;
        this.fieldFactoryMap = fieldFactories.stream()
                .collect(Collectors.toUnmodifiableMap(FieldFactory::fieldVariant, f -> f));
        this.winCheckerFactoryMap = winCheckerFactories.stream()
                .collect(Collectors.toUnmodifiableMap(WinCheckerFactory::winCheckerVariant, f -> f));
    }

    @Override
    public List<DoroGame> getAll() {
        return repository.findAllByRemovedFalse().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<DoroGame> getById(UUID id) {
        return doroGameCache.containsKey(id)
                ? Optional.ofNullable(doroGameCache.get(id))
                : repository.findByIdAndRemovedFalse(id).map(this::toDomain);
    }

    @Override
    public DoroGame save(DoroGame game) {
        doroGameCache.put(game.getId(), game);
        if (game.isFinished() || game.getMoveCounter() % movesBatchSize == 0) {
            DoroGameEntity entity = mapper.fromDomain(game);
            DoroGameEntity saved = repository.save(entity);
            return toDomain(saved);
        }
        return game;
    }

    @Override
    public void deleteById(UUID id) {
        doroGameCache.remove(id);
        repository.findByIdAndRemovedFalse(id).ifPresent(e -> {
            e.setRemoved(true);
            repository.save(e);
        });
    }

    @Override
    public List<DoroGame> getByUserIdAndFinishedTrue(UUID userId) {
        return repository.findAllByPlayerIdAndRemovedFalseAndFinishedTrue(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    private DoroGame toDomain(DoroGameEntity entity) {
        Field field = fieldFactoryMap.get(entity.getFieldVariant()).createField();
        WinChecker winChecker = winCheckerFactoryMap.get(entity.getWinCheckerVariant()).createWinChecker();
        List<UUID> chipIds = entity.getStartPositions().stream().map(c -> c.getId()).toList();
        List<ChipMoveEntity> lastMoves = chipMoveEntityRepository.findLastMovesForChips(chipIds);
        return mapper.toDomain(entity, field, winChecker, lastMoves);
    }
}
