package ru.kirillvodu.dorogame.game.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.InvitationRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.services.UserServiceAbstraction;
import ru.kirillvodu.dorogame.game.application.contracts.commands.AcceptPublicInvitationCommand;
import ru.kirillvodu.dorogame.game.application.contracts.commands.CreatePublicInvitationCommand;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.application.factories.games.DoroGameFactory;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;

import java.util.List;

@Service
public class PublicInvitationService {

    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private DoroGameRepository doroGameRepository;
    @Autowired
    private UserServiceAbstraction userServiceAbstraction;
    @Autowired
    private DoroGameFactory doroGameFactory;

    public Invitation createPublicInvitation(CreatePublicInvitationCommand command) {
        UserReadModel user = userServiceAbstraction.getById(command.userId());
        Invitation invitation = Invitation.createPublic(user, command.gameConfig());
        return invitationRepository.save(invitation);
    }

    public List<Invitation> getAllPublicInvitations() {
        return invitationRepository.getAll().stream()
                .filter(i -> !i.isPrivate())
                .toList();
    }

    public DoroGame acceptPublicInvitation(AcceptPublicInvitationCommand command) {
        Invitation invitation = invitationRepository.getById(command.invitationId())
                .orElseThrow(() -> new ObjectNotFoundException(command.invitationId(), "Invitation"));

        if (invitation.isPrivate()) {
            throw new ObjectNotFoundException(command.invitationId(), "Invitation");
        }

        UserReadModel acceptingUser = userServiceAbstraction.getById(command.acceptingUserId());

        return createGameAndDeleteInvitation(invitation, acceptingUser);
    }

    @Transactional
    private DoroGame createGameAndDeleteInvitation (Invitation invitation, UserReadModel inviter) {
        int existed = invitationRepository.atomicDeleteById(invitation.id());
        if (existed == 0) {
            throw new ObjectNotFoundException(invitation.id(), "Invitation");
        }

        DoroGame game = doroGameFactory.createDoroGame(inviter, invitation);
        return doroGameRepository.save(game);
    }
}
