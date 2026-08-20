package ru.kirillvodu.dorogame.game.infrastructure.persistence.mappers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.domain.model.game.GameConfig;
import ru.kirillvodu.dorogame.game.domain.model.invitation.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;
import ru.kirillvodu.dorogame.game.infrastructure.persistence.entities.InvitationEntity;

@Component
public class InvitationEntityMapper {

    public Invitation toDomain(InvitationEntity entity) {
        UserReadModel user = new UserReadModel(entity.getUserId(), entity.getUserName());
        GameConfig config = new GameConfig(entity.getFieldVariant(), entity.getWinCheckerVariant(), entity.getTurn());
        return new Invitation(entity.getId(), user, entity.getTargetUserId(), config);
    }

    public InvitationEntity fromDomain(Invitation invitation) {
        InvitationEntity entity = InvitationEntity.builder()
                .userId(invitation.user().id())
                .userName(invitation.user().name())
                .targetUserId(invitation.targetUserId())
                .fieldVariant(invitation.gameConfig().field())
                .winCheckerVariant(invitation.gameConfig().winChecker())
                .turn(invitation.gameConfig().turn())
                .build();
        entity.setId(invitation.id());
        return entity;
    }
}
