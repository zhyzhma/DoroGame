package ru.kirillvodu.dorogame.game.application.abstractions.repositories;

import ru.kirillvodu.dorogame.game.domain.model.invitation.Invitation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository {
    List<Invitation> getAll();
    Optional<Invitation> getById(UUID id);
    Invitation save(Invitation invitation);
    void deleteById(UUID id);
    int atomicDeleteById(UUID id);
}
