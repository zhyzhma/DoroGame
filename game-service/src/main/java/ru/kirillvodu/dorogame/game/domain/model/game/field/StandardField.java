package ru.kirillvodu.dorogame.game.domain.model.game.field;

import ru.kirillvodu.dorogame.game.domain.model.game.Coords;
import ru.kirillvodu.dorogame.game.domain.model.game.Player;

import java.util.ArrayList;
import java.util.List;

public class StandardField implements Field {

    @Override
    public FieldVariant getFieldVariant() {
        return FieldVariant.STANDARD;
    }

    @Override
    public boolean areCoordsValid(Coords coords) {
        int x = coords.x();
        int y = coords.y();
        return (x >= 0) && (x <= 8) && (y <= 5) && (y >= (x % 2)) &&
                ((x != 0) || (y != 2)) && ((x != 0) || (y != 3)) &&
                ((x != 4) || (y != 2)) && ((x != 4) || (y != 3)) &&
                ((x != 8) || (y != 2)) && ((x != 8) || (y != 3));
    }

    @Override
    public List<Coords> getReachableFrom(Coords coords, List<Player> players) {
        List<Coords> result = new ArrayList<>();
        addReachableRight(result, coords, players);
        addReachableLeft(result, coords, players);
        addReachableUpRight(result, coords, players);
        addReachableUpLeft(result, coords, players);
        addReachableDownRight(result, coords, players);
        addReachableDownLeft(result, coords, players);
        return result;
    }

    private void addReachableRight(List<Coords> list, Coords coords, List<Player> players) {
        Coords next = new Coords(coords.x(), coords.y() + 1);
        if (areCoordsValid(next) && players.stream().noneMatch(p -> p.isCellOccupied(next))) {
            list.add(next);
            addReachableRight(list, next, players);
        }
    }

    private void addReachableLeft(List<Coords> list, Coords coords, List<Player> players) {
        Coords next = new Coords(coords.x(), coords.y() - 1);
        if (areCoordsValid(next) && players.stream().noneMatch(p -> p.isCellOccupied(next))) {
            list.add(next);
            addReachableLeft(list, next, players);
        }
    }

    private void addReachableUpRight(List<Coords> list, Coords coords, List<Player> players) {
        int y = (coords.x() % 2 == 0) ? coords.y() + 1 : coords.y();
        Coords next = new Coords(coords.x() - 1, y);
        if (areCoordsValid(next) && players.stream().noneMatch(p -> p.isCellOccupied(next))) {
            list.add(next);
            addReachableUpRight(list, next, players);
        }
    }

    private void addReachableUpLeft(List<Coords> list, Coords coords, List<Player> players) {
        int y = (coords.x() % 2 == 1) ? coords.y() - 1 : coords.y();
        Coords next = new Coords(coords.x() - 1, y);
        if (areCoordsValid(next) && players.stream().noneMatch(p -> p.isCellOccupied(next))) {
            list.add(next);
            addReachableUpLeft(list, next, players);
        }
    }

    private void addReachableDownRight(List<Coords> list, Coords coords, List<Player> players) {
        int y = (coords.x() % 2 == 0) ? coords.y() + 1 : coords.y();
        Coords next = new Coords(coords.x() + 1, y);
        if (areCoordsValid(next) && players.stream().noneMatch(p -> p.isCellOccupied(next))) {
            list.add(next);
            addReachableDownRight(list, next, players);
        }
    }

    private void addReachableDownLeft(List<Coords> list, Coords coords, List<Player> players) {
        int y = (coords.x() % 2 == 1) ? coords.y() - 1 : coords.y();
        Coords next = new Coords(coords.x() + 1, y);
        if (areCoordsValid(next) && players.stream().noneMatch(p -> p.isCellOccupied(next))) {
            list.add(next);
            addReachableDownLeft(list, next, players);
        }
    }
}
