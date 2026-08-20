package ru.kirillvodu.dorogame.game.application.factories.fields;

import ru.kirillvodu.dorogame.game.domain.model.game.field.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.game.field.Field;

public interface FieldFactory {
    FieldVariant fieldVariant();
    Field createField();
}
