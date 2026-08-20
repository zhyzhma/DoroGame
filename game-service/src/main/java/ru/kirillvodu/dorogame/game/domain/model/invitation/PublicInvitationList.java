package ru.kirillvodu.dorogame.game.domain.model.invitation;

import lombok.Getter;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PublicInvitationList {
    @Getter
    private final List<Invitation> invitations = new ArrayList<>();

    public void addInvitation(Invitation invitation) {
        invitations.add(invitation);
    }

    public Invitation acceptInvitation(UserReadModel user, UUID invitationId) {
        Invitation invitation = invitations.stream()
                .filter(x -> x.id().equals(invitationId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Invitation not found: " + invitationId));
        invitations.remove(invitation);
        return invitation;
    }
}
