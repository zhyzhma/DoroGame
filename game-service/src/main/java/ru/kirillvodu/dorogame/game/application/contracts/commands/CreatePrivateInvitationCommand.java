package ru.kirillvodu.dorogame.game.application.contracts.commands;

import ru.kirillvodu.dorogame.game.domain.model.GameConfig;

import java.util.UUID;

public record CreatePrivateInvitationCommand(UUID inviterId, UUID opponentId, GameConfig gameConfig) {
}
