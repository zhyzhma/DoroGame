package ru.kirillvodu.dorogame.game.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.InvitationRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.services.UserServiceAbstraction;
import ru.kirillvodu.dorogame.game.application.contracts.commands.AcceptPrivateInvitationCommand;
import ru.kirillvodu.dorogame.game.application.contracts.commands.CreatePrivateInvitationCommand;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.application.factories.games.DoroGameFactory;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;

import java.util.List;
import java.util.UUID;

@Service
public class PrivateInvitationService {

    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private DoroGameRepository doroGameRepository;
    @Autowired
    private UserServiceAbstraction userServiceAbstraction;
    @Autowired
    private DoroGameFactory doroGameFactory;

    public Invitation createPrivateInvitation(CreatePrivateInvitationCommand command) {
        UserReadModel inviter = userServiceAbstraction.getById(command.inviterId());
        Invitation invitation = Invitation.createPrivate(inviter, command.opponentId(), command.gameConfig());
        return invitationRepository.save(invitation);
    }

    public List<Invitation> getInvitationsForUser(UUID userId) {
        return invitationRepository.getAll().stream()
                .filter(i -> i.isPrivate() && i.targetUserId().equals(userId))
                .toList();
    }

    public DoroGame acceptPrivateInvitation(AcceptPrivateInvitationCommand command) {
        Invitation invitation = invitationRepository.getById(command.invitationId())
                .orElseThrow(() -> new ObjectNotFoundException(command.invitationId(), "Invitation"));

        if (!invitation.isPrivate() || !invitation.targetUserId().equals(command.acceptingUserId())) {
            throw new IllegalStateException("Invitation does not belong to this user");
        }

        UserReadModel acceptingUser = userServiceAbstraction.getById(command.acceptingUserId());

        DoroGame game = doroGameFactory.createDoroGame(acceptingUser, invitation);
        DoroGame savedGame = doroGameRepository.save(game);

        invitationRepository.deleteById(invitation.id());

        return savedGame;
    }
}
