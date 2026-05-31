package ru.kirillvodu.dorogame.game.application.contracts.commands;

import ru.kirillvodu.dorogame.game.domain.model.Coords;

import java.util.UUID;

public record MakeMoveCommand(UUID gameId, UUID playerId, UUID chipId, Coords coords) {
}
