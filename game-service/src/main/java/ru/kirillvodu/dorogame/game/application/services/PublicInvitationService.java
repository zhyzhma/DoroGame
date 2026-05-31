package ru.kirillvodu.dorogame.game.application.services;

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

        UserReadModel acceptingUser = userServiceAbstraction.getById(command.acceptingUserId());

        DoroGame game = doroGameFactory.createDoroGame(acceptingUser, invitation);
        DoroGame savedGame = doroGameRepository.save(game);

        invitationRepository.deleteById(invitation.id());

        return savedGame;
    }
}
