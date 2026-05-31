package ru.kirillvodu.dorogame.game.application.contracts.commands;

import java.util.UUID;

public record AcceptPublicInvitationCommand(UUID invitationId, UUID acceptingUserId) {
}
