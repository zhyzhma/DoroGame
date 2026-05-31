package ru.kirillvodu.dorogame.game.domain.model.enums;

import java.util.Arrays;

public enum WinCheckerVariant {
    STANDARD,
    RAPID;

    public static WinCheckerVariant fromString(String name) {
        return Arrays.stream(WinCheckerVariant.values())
                .filter(e -> e.name().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown WinCheckerVariant: " + name));
    }
}
