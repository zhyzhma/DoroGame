package ru.kirillvodu.dorogame.game.application.factories.winCheckers;

import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinChecker;

public interface WinCheckerFactory {
    WinCheckerVariant winCheckerVariant();
    WinChecker createWinChecker();
}
