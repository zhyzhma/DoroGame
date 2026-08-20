package ru.kirillvodu.dorogame.game.application.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.contracts.DTO.read.InvitationReadDTO;
import ru.kirillvodu.dorogame.game.domain.model.invitation.Invitation;

@Component
public class InvitationMapper {

    public InvitationReadDTO toDto(Invitation invitation) {
        return new InvitationReadDTO(
                invitation.id(),
                invitation.user().id(),
                invitation.gameConfig().field().name(),
                invitation.gameConfig().winChecker().name()
        );
    }
}
