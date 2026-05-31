package ru.kirillvodu.dorogame.user.application.abstractions.repositories;

import ru.kirillvodu.dorogame.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> getAll();
    Optional<User> getById(UUID id);
    List<User> getByIds(List<UUID> ids);
    User save(User user);
    void deleteById(UUID id);
}
