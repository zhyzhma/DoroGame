package ru.kirillvodu.dorogame.game.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.factories.fields.FieldFactory;
import ru.kirillvodu.dorogame.game.application.factories.winCheckers.WinCheckerFactory;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.domain.model.field.Field;
import ru.kirillvodu.dorogame.game.domain.model.winchecker.WinChecker;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.DoroGameEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.DoroGameEntityRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class DoroGameRepositoryAdapter implements DoroGameRepository {

    private final DoroGameEntityRepository repository;
    private final Map<FieldVariant, FieldFactory> fieldFactoryMap;
    private final Map<WinCheckerVariant, WinCheckerFactory> winCheckerFactoryMap;

    public DoroGameRepositoryAdapter(DoroGameEntityRepository repository,
                                     List<FieldFactory> fieldFactories,
                                     List<WinCheckerFactory> winCheckerFactories) {
        this.repository = repository;
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
        return repository.findByIdAndRemovedFalse(id).map(this::toDomain);
    }

    @Override
    public DoroGame save(DoroGame game) {
        DoroGameEntity entity = DoroGameEntity.fromDomain(game);
        DoroGameEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        repository.findByIdAndRemovedFalse(id).ifPresent(e -> {
            e.setRemoved(true);
            repository.save(e);
        });
    }

    private DoroGame toDomain(DoroGameEntity entity) {
        Field field = fieldFactoryMap.get(entity.getFieldVariant()).createField();
        WinChecker winChecker = winCheckerFactoryMap.get(entity.getWinCheckerVariant()).createWinChecker();
        return entity.toDomain(field, winChecker);
    }
}
