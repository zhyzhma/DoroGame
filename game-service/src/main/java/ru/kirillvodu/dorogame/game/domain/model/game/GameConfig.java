package ru.kirillvodu.dorogame.game.domain.model.game;

import ru.kirillvodu.dorogame.game.domain.model.game.field.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.winchecker.WinCheckerVariant;

public record GameConfig(FieldVariant field, WinCheckerVariant winChecker, int turn) {
}
