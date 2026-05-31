package ru.kirillvodu.dorogame.game.application.contracts.DTO.read;

import java.util.UUID;

public record InvitationReadDTO(UUID id, UUID userId, String fieldVariant, String winCheckerVariant) {
}
