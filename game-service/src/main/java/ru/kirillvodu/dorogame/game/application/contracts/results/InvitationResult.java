package ru.kirillvodu.dorogame.game.application.contracts.results;

import java.util.UUID;

public record InvitationResult(UUID id, UUID userId, String fieldVariant, String winCheckerVariant) {
}
