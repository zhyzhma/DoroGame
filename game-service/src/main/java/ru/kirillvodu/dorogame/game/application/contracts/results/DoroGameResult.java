package ru.kirillvodu.dorogame.game.application.contracts.results;

import java.util.UUID;

public record DoroGameResult(UUID id, UUID player1Id, UUID player2Id, boolean finished, int winner, int turn) {
}
