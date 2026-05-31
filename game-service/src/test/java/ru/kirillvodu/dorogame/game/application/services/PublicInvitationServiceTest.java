package ru.kirillvodu.dorogame.game.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.DoroGameRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.InvitationRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.services.UserServiceAbstraction;
import ru.kirillvodu.dorogame.game.application.contracts.commands.AcceptPublicInvitationCommand;
import ru.kirillvodu.dorogame.game.application.contracts.commands.CreatePublicInvitationCommand;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.application.factories.games.DoroGameFactory;
import ru.kirillvodu.dorogame.game.domain.model.*;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicInvitationServiceTest {

    @Mock private InvitationRepository invitationRepository;
    @Mock private DoroGameRepository doroGameRepository;
    @Mock private UserServiceAbstraction userServiceAbstraction;
    @Mock private DoroGameFactory doroGameFactory;

    @InjectMocks private PublicInvitationService publicInvitationService;

    private final GameConfig config = new GameConfig(FieldVariant.STANDARD, WinCheckerVariant.STANDARD, 1);

    @Test
    void createPublicInvitation_savesAndReturnsInvitation() {
        UUID userId = UUID.randomUUID();
        UserReadModel user = new UserReadModel(userId, "Player");
        Invitation saved = Invitation.createPublic(user, config);

        when(userServiceAbstraction.getById(userId)).thenReturn(user);
        when(invitationRepository.save(any())).thenReturn(saved);

        Invitation result = publicInvitationService.createPublicInvitation(
                new CreatePublicInvitationCommand(userId, config));

        assertThat(result.user()).isEqualTo(user);
        assertThat(result.isPrivate()).isFalse();
        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    void acceptPublicInvitation_createsAndReturnsGame() {
        UUID invitationId = UUID.randomUUID();
        UUID acceptingUserId = UUID.randomUUID();
        UserReadModel inviter = new UserReadModel(UUID.randomUUID(), "Inviter");
        UserReadModel accepter = new UserReadModel(acceptingUserId, "Accepter");
        Invitation invitation = new Invitation(invitationId, inviter, null, config);
        DoroGame game = mock(DoroGame.class);

        when(invitationRepository.getById(invitationId)).thenReturn(Optional.of(invitation));
        when(userServiceAbstraction.getById(acceptingUserId)).thenReturn(accepter);
        when(doroGameFactory.createDoroGame(accepter, invitation)).thenReturn(game);
        when(doroGameRepository.save(game)).thenReturn(game);

        DoroGame result = publicInvitationService.acceptPublicInvitation(
                new AcceptPublicInvitationCommand(invitationId, acceptingUserId));

        assertThat(result).isSameAs(game);
        verify(invitationRepository).deleteById(invitationId);
    }

    @Test
    void acceptPublicInvitation_invitationNotFound_throwsObjectNotFoundException() {
        UUID invitationId = UUID.randomUUID();
        when(invitationRepository.getById(invitationId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> publicInvitationService.acceptPublicInvitation(
                        new AcceptPublicInvitationCommand(invitationId, UUID.randomUUID())));
    }

    @Test
    void getAllPublicInvitations_returnsOnlyPublicOnes() {
        UserReadModel user = new UserReadModel(UUID.randomUUID(), "User");
        Invitation pub = Invitation.createPublic(user, config);
        Invitation priv = Invitation.createPrivate(user, UUID.randomUUID(), config);

        when(invitationRepository.getAll()).thenReturn(List.of(pub, priv));

        List<Invitation> result = publicInvitationService.getAllPublicInvitations();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().isPrivate()).isFalse();
    }
}
