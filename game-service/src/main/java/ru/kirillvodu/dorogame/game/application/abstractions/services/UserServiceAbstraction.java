package ru.kirillvodu.dorogame.game.application.abstractions.services;

import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;

import java.util.List;
import java.util.UUID;

public interface UserServiceAbstraction {
    UserReadModel getById(UUID userId);
    List<UserReadModel> getByIds(List<UUID> userIds);
}
