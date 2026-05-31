package ru.kirillvodu.dorogame.game.application.contracts.DTO.create;

import java.util.UUID;

public record PrivateInvitationCreateDTO(UUID opponentId, String fieldVariant, String winCheckerVariant, int turn) {
}
