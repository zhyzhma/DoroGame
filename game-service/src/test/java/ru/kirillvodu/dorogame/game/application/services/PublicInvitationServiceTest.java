package ru.kirillvodu.dorogame.game.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kirillvodu.dorogame.game.application.abstractions.repositories.InvitationRepository;
import ru.kirillvodu.dorogame.game.application.abstractions.services.UserServiceAbstraction;
import ru.kirillvodu.dorogame.game.application.contracts.commands.AcceptPublicInvitationCommand;
import ru.kirillvodu.dorogame.game.application.contracts.commands.CreatePublicInvitationCommand;
import ru.kirillvodu.dorogame.game.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.game.domain.model.game.GameConfig;
import ru.kirillvodu.dorogame.game.domain.model.game.field.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.invitation.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;

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
    @Mock private UserServiceAbstraction userServiceAbstraction;
    @Mock private InvitationAcceptanceService invitationAcceptanceService;

    @InjectMocks private PublicInvitationService publicInvitationService;

    private final GameConfig config = new GameConfig(FieldVariant.STANDARD, WinCheckerVariant.STANDARD, 1);

    @Test
    void createPublicInvitation_savesAndReturnsInvitation_whenCommandValid() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserReadModel user = new UserReadModel(userId, "Player");
        Invitation saved = Invitation.createPublic(user, config);

        when(userServiceAbstraction.getById(userId)).thenReturn(user);
        when(invitationRepository.save(any())).thenReturn(saved);

        // Act
        Invitation result = publicInvitationService.createPublicInvitation(
                new CreatePublicInvitationCommand(userId, config));

        // Assert
        assertThat(result.user()).isEqualTo(user);
        assertThat(result.isPrivate()).isFalse();
        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    void acceptPublicInvitation_createsAndReturnsGame_whenInvitationExists() {
        // Arrange
        UUID invitationId = UUID.randomUUID();
        UUID acceptingUserId = UUID.randomUUID();
        UserReadModel inviter = new UserReadModel(UUID.randomUUID(), "Inviter");
        UserReadModel accepter = new UserReadModel(acceptingUserId, "Accepter");
        Invitation invitation = new Invitation(invitationId, inviter, null, config);
        DoroGame game = mock(DoroGame.class);

        when(invitationRepository.getById(invitationId)).thenReturn(Optional.of(invitation));
        when(userServiceAbstraction.getById(acceptingUserId)).thenReturn(accepter);
        when(invitationAcceptanceService.createGameAndDeleteInvitation(invitation, accepter)).thenReturn(game);

        // Act
        DoroGame result = publicInvitationService.acceptPublicInvitation(
                new AcceptPublicInvitationCommand(invitationId, acceptingUserId));

        // Assert
        assertThat(result).isSameAs(game);
        verify(invitationAcceptanceService).createGameAndDeleteInvitation(invitation, accepter);
    }

    @Test
    void acceptPublicInvitation_throwsObjectNotFoundException_whenInvitationNotFound() {
        // Arrange
        UUID invitationId = UUID.randomUUID();
        when(invitationRepository.getById(invitationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class,
                () -> publicInvitationService.acceptPublicInvitation(
                        new AcceptPublicInvitationCommand(invitationId, UUID.randomUUID())));
    }

    @Test
    void getAllPublicInvitations_returnsOnlyPublicInvitations_whenMixedInvitationsExist() {
        // Arrange
        UserReadModel user = new UserReadModel(UUID.randomUUID(), "User");
        Invitation pub = Invitation.createPublic(user, config);
        Invitation priv = Invitation.createPrivate(user, UUID.randomUUID(), config);

        when(invitationRepository.getAll()).thenReturn(List.of(pub, priv));

        // Act
        List<Invitation> result = publicInvitationService.getAllPublicInvitations();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().isPrivate()).isFalse();
    }
}
