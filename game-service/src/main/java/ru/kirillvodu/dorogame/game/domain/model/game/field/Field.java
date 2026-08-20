package ru.kirillvodu.dorogame.game.domain.model.game.field;

import ru.kirillvodu.dorogame.game.domain.model.game.Coords;
import ru.kirillvodu.dorogame.game.domain.model.game.Player;

import java.util.List;

public interface Field {
    FieldVariant getFieldVariant();
    boolean areCoordsValid(Coords coords);
    List<Coords> getReachableFrom(Coords coords, List<Player> players);
}
