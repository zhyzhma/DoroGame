package ru.kirillvodu.dorogame.game.domain.model;

import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;

public record GameConfig(FieldVariant field, WinCheckerVariant winChecker, int turn) {
}
