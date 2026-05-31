package ru.kirillvodu.dorogame.game.application.contracts.DTO.create;

import java.util.UUID;

public record MakeMoveDTO(UUID chipId, int x, int y) {
}
