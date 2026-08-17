package ru.kirillvodu.dorogame.user.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.UserRepository;
import ru.kirillvodu.dorogame.user.domain.model.User;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.entities.UserEntity;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.mappers.UserEntityMapper;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.repositories.UserEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserEntityRepository repository;
    private final UserEntityMapper mapper;

    public UserRepositoryAdapter(UserEntityRepository repository, UserEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<User> getAll() {
        return repository.findAllByRemovedFalse().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> getById(UUID keycloakId) {
        return repository.findByKeycloakId(keycloakId).map(mapper::toDomain);
    }

    @Override
    public List<User> getByIds(List<UUID> keycloakIds) {
        return repository.findAllByKeycloakIdIn(keycloakIds).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        UserEntity entity = repository.findByKeycloakId(user.getId())
                .map(existing -> {
                    existing.setName(user.getName());
                    existing.setScore(user.getScore());
                    return existing;
                })
                .orElse(mapper.fromDomain(user));
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(UUID keycloakId) {
        repository.findByKeycloakId(keycloakId).ifPresent(e -> {
            e.setRemoved(true);
            repository.save(e);
        });
    }
}

