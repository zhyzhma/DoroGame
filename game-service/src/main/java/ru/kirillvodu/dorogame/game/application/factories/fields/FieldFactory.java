package ru.kirillvodu.dorogame.game.application.factories.fields;

import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.field.Field;

public interface FieldFactory {
    FieldVariant fieldVariant();
    Field createField();
}
