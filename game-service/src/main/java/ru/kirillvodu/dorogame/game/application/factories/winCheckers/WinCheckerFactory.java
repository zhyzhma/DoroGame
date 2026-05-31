package ru.kirillvodu.dorogame.game.application.factories.winCheckers;

import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.domain.model.winchecker.WinChecker;

public interface WinCheckerFactory {
    WinCheckerVariant winCheckerVariant();
    WinChecker createWinChecker();
}
