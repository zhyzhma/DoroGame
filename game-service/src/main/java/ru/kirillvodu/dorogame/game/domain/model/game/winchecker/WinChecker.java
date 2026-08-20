package ru.kirillvodu.dorogame.game.domain.model.game.winchecker;

import ru.kirillvodu.dorogame.game.domain.model.game.Coords;

import java.util.List;

public interface WinChecker {
    WinCheckerVariant getWinCheckerVariant();
    boolean checkWin(List<Coords> coords);
}
