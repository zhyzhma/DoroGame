package ru.kirillvodu.dorogame.game.application.factories.fields;

import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.field.Field;
import ru.kirillvodu.dorogame.game.domain.model.field.StandardField;

@Component
public class StandardFieldFactory implements FieldFactory {

    @Override
    public FieldVariant fieldVariant() {
        return FieldVariant.STANDARD;
    }

    @Override
    public Field createField() {
        return new StandardField();
    }
}
