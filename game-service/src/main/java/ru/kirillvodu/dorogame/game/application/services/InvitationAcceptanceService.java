package ru.kirillvodu.dorogame.game.application.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.InvitationRepository;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.application.factories.games.DoroGameFactory;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.invitation.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;

@Service
public class InvitationAcceptanceService {

    private final InvitationRepository invitationRepository;
    private final DoroGameRepository doroGameRepository;
    private final DoroGameFactory doroGameFactory;

    public InvitationAcceptanceService(InvitationRepository invitationRepository,
                                       DoroGameRepository doroGameRepository,
                                       DoroGameFactory doroGameFactory) {
        this.invitationRepository = invitationRepository;
        this.doroGameRepository = doroGameRepository;
        this.doroGameFactory = doroGameFactory;
    }

    @Transactional
    public DoroGame createGameAndDeleteInvitation(Invitation invitation, UserReadModel acceptingUser) {
        int existed = invitationRepository.atomicDeleteById(invitation.id());
        if (existed == 0) {
            throw new ObjectNotFoundException(invitation.id(), "Invitation");
        }

        DoroGame game = doroGameFactory.createDoroGame(acceptingUser, invitation);
        return doroGameRepository.save(game);
    }
}
