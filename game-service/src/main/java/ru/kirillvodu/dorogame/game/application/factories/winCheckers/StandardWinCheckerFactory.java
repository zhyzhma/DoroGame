package ru.kirillvodu.dorogame.game.application.factories.winCheckers;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinCheckerVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.StandardWinChecker;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinChecker;

@Component
public class StandardWinCheckerFactory implements WinCheckerFactory {

    @Override
    public WinCheckerVariant winCheckerVariant() {
        return WinCheckerVariant.STANDARD;
    }

    @Override
    public WinChecker createWinChecker() {
        return new StandardWinChecker();
    }
}
