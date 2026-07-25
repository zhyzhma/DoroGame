package ru.kirillvodu.dorogame.game.domain.model.winchecker;

import ru.kirillvodu.dorogame.game.domain.model.Coords;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;

import java.util.List;

public interface WinChecker {
    WinCheckerVariant getWinCheckerVariant();
    boolean checkWin(List<Coords> coords);
}
