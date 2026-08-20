package ru.kirillvodu.dorogame.game.application.contracts.commands;

import ru.kirillvodu.dorogame.game.domain.model.game.GameConfig;

import java.util.UUID;

public record CreatePublicInvitationCommand(UUID userId, GameConfig gameConfig) {
}
