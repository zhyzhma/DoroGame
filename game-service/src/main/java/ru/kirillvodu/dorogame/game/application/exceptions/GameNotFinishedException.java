package ru.kirillvodu.dorogame.game.application.exceptions;

import java.util.UUID;

public class GameNotFinishedException extends RuntimeException {
    public GameNotFinishedException(UUID gameId) {
        super("Game " + gameId + " is not finished");
    }
}
