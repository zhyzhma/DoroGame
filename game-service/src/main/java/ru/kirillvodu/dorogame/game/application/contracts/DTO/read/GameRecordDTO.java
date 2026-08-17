package ru.kirillvodu.dorogame.game.application.contracts.DTO.read;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record GameRecordDTO(UUID id, UUID player1Id, String player1Name, UUID player2Id, String player2Name,
                            List<ChipDTO> startPositions1, List<ChipDTO> startPositions2,
                            int winner, List<ChipMoveDTO> chipMoves) {
}
