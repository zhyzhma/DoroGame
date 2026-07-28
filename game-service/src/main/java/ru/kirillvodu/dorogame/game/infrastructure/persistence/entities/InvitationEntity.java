package ru.kirillvodu.dorogame.game.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.kirillvodu.dorogame.game.domain.model.GameConfig;
import ru.kirillvodu.dorogame.game.domain.model.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;

import java.util.UUID;

@Entity
@Table(name = "invitations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "target_user_id")
    private UUID targetUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_variant", nullable = false)
    private FieldVariant fieldVariant;

    @Enumerated(EnumType.STRING)
    @Column(name = "win_checker_variant", nullable = false)
    private WinCheckerVariant winCheckerVariant;

    @Column(name = "turn", nullable = false)
    private int turn;

    public Invitation toDomain() {
        UserReadModel user = new UserReadModel(userId, userName);
        GameConfig config = new GameConfig(fieldVariant, winCheckerVariant, turn);
        return new Invitation(getId(), user, targetUserId, config);
    }

    public static InvitationEntity fromDomain(Invitation invitation) {
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
