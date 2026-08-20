package ru.kirillvodu.dorogame.game.domain.model.game.field;

import java.util.Arrays;

public enum FieldVariant {
    STANDARD;

    public static FieldVariant fromString(String name) {
        return Arrays.stream(FieldVariant.values())
                .filter(e -> e.name().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown FieldVariant: " + name));
    }
}
