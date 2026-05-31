package ru.kirillvodu.dorogame.game.application.contracts.commands;

import java.util.UUID;

public record AcceptPrivateInvitationCommand(UUID invitationId, UUID acceptingUserId) {
}
