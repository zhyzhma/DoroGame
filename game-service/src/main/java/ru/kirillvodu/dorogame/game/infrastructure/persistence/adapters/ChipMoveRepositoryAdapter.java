package ru.kirillvodu.dorogame.game.infrastructure.persistence.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.ChipMoveRepository;
import ru.kirillvodu.dorogame.game.domain.model.history.ChipMove;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.ChipMoveEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.mappers.ChipMoveEntityMapper;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.ChipEntityRepository;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.ChipMoveEntityRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class ChipMoveRepositoryAdapter implements ChipMoveRepository {

    @Autowired
    private ChipMoveEntityRepository repository;

    @Autowired
    private ChipMoveEntityMapper chipMoveMapper;

    @Autowired
    private ChipEntityRepository chipEntityRepository;

    @Override
    public List<ChipMove> getByGameId(UUID gameId) {
        return repository.findByChipEntity_GameId(gameId).stream().map(chipMoveMapper::toDomain).toList();
    }

    @Override
    public ChipMove save(ChipMove chipMove) {
        ChipEntity chipRef = chipEntityRepository.getReferenceById(chipMove.chip().getId());
        ChipMoveEntity entity = chipMoveMapper.toEntity(chipMove, chipRef);
        return chipMoveMapper.toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
