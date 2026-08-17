package ru.kirillvodu.dorogame.game.application.contracts.DTO.read;

import java.util.List;
import java.util.UUID;

public record DoroGameDTO(UUID id, UUID player1Id, String player1Name, UUID player2Id, String player2Name,
                          List<ChipDTO> chips1, List<ChipDTO> chips2,
                          boolean finished, int winner, int turn) {
}
