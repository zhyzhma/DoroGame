package ru.kirillvodu.dorogame.game.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.InvitationRepository;
import ru.kirillvodu.dorogame.game.domain.model.Invitation;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.InvitationEntity;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.InvitationEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InvitationRepositoryAdapter implements InvitationRepository {

    private final InvitationEntityRepository repository;

    public InvitationRepositoryAdapter(InvitationEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Invitation> getAll() {
        return repository.findAllByRemovedFalse().stream()
                .map(InvitationEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Invitation> getById(UUID id) {
        return repository.findByIdAndRemovedFalse(id).map(InvitationEntity::toDomain);
    }

    @Override
    public Invitation save(Invitation invitation) {
        return repository.save(InvitationEntity.fromDomain(invitation)).toDomain();
    }

    @Override
    public void deleteById(UUID id) {
        repository.findByIdAndRemovedFalse(id).ifPresent(e -> {
            e.setRemoved(true);
            repository.save(e);
        });
    }

    @Override
    public int atomicDeleteById(UUID id) {
        return repository.updateByRemovedFalse(id);
    }
}
