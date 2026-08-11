package ru.kirillvodu.dorogame.stats.application.exceptions;

import java.util.UUID;

public class DuplicateGameResultException extends RuntimeException {
    public DuplicateGameResultException(UUID gameId) {
        super("Game result already recorded: " + gameId);
    }
}
