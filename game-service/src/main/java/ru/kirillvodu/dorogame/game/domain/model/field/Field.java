package ru.kirillvodu.dorogame.game.domain.model.field;

import ru.kirillvodu.dorogame.game.domain.model.Coords;
import ru.kirillvodu.dorogame.game.domain.model.Player;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;

import java.util.List;

public interface Field {
    FieldVariant getFieldVariant();
    boolean areCoordsValid(Coords coords);
    List<Coords> getReachableFrom(Coords coords, List<Player> players);
}
