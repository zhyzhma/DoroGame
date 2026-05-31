package ru.kirillvodu.dorogame.user.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import ru.kirillvodu.dorogame.user.application.abstractions.repositories.UserRepository;
import ru.kirillvodu.dorogame.user.domain.model.User;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.entities.UserEntity;
import ru.kirillvodu.dorogame.user.infrastructure.persistence.repositories.UserEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserEntityRepository repository;

    public UserRepositoryAdapter(UserEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll() {
        return repository.findAllByRemovedFalse().stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<User> getById(UUID keycloakId) {
        return repository.findByKeycloakId(keycloakId).map(UserEntity::toDomain);
    }

    @Override
    public List<User> getByIds(List<UUID> keycloakIds) {
        return repository.findAllByKeycloakIdIn(keycloakIds).stream()
                .map(UserEntity::toDomain)
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
                .orElse(UserEntity.fromDomain(user));
        return repository.save(entity).toDomain();
    }

    @Override
    public void deleteById(UUID keycloakId) {
        repository.findByKeycloakId(keycloakId).ifPresent(e -> {
            e.setRemoved(true);
            repository.save(e);
        });
    }
}
