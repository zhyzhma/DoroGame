package ru.kirillvodu.dorogame.game.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.InvitationRepository;
import ru.kirillvodu.dorogame.game.domain.model.invitation.Invitation;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.mappers.InvitationEntityMapper;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.repositories.InvitationEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InvitationRepositoryAdapter implements InvitationRepository {

    private final InvitationEntityRepository repository;
    private final InvitationEntityMapper mapper;

    public InvitationRepositoryAdapter(InvitationEntityRepository repository, InvitationEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Invitation> getAll() {
        return repository.findAllByRemovedFalse().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Invitation> getById(UUID id) {
        return repository.findByIdAndRemovedFalse(id).map(mapper::toDomain);
    }

    @Override
    public Invitation save(Invitation invitation) {
        return mapper.toDomain(repository.save(mapper.fromDomain(invitation)));
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
