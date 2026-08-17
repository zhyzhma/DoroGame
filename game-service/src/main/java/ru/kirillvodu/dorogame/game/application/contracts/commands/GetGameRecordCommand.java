package ru.kirillvodu.dorogame.game.application.contracts.commands;

import java.util.UUID;

public record GetGameRecordCommand(UUID gameId, UUID userId) {
}
