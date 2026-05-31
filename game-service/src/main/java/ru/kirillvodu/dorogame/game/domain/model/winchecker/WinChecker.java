package ru.kirillvodu.dorogame.game.domain.model.winchecker;

import ru.kirillvodu.dorogame.game.domain.model.Coords;

import java.util.List;

public interface WinChecker {
    boolean checkWin(List<Coords> coords);
}
