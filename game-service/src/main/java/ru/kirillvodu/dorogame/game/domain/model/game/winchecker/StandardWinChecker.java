package ru.kirillvodu.dorogame.game.domain.model.game.winchecker;

import ru.kirillvodu.dorogame.game.domain.model.game.Coords;

import java.util.Comparator;
import java.util.List;

public class StandardWinChecker implements WinChecker {

    @Override
    public WinCheckerVariant getWinCheckerVariant() {
        return WinCheckerVariant.STANDARD;
    }

    @Override
    public boolean checkWin(List<Coords> coords) {
        if (coords.size() != 4) {
            throw new IllegalArgumentException("Standard win checker requires 4 coords");
        }
        List<Coords> s = coords.stream()
                .sorted(Comparator.comparingInt(Coords::x).thenComparingInt(Coords::y)).toList();

        Coords c0 = s.get(0), c1 = s.get(1), c2 = s.get(2), c3 = s.get(3);

        boolean horizontal = c0.x() == c1.x() && c1.x() == c2.x() && c2.x() == c3.x();
        boolean diagonal = (c0.x() - c1.x() == 1) && (c1.x() - c2.x() == 1) && (c2.x() - c3.x() == 1);
        boolean line = (c1.y() - c0.y() == 1) && (c2.y() - c1.y() == 1) && (c3.y() - c2.y() == 1);
        boolean slash = ((c1.y() - c0.y() == 1) && (c2.y() == c1.y()) && (c3.y() - c2.y() == 1))
                || ((c1.y() == c0.y()) && (c2.y() - c1.y() == 1) && (c3.y() == c2.y()));
        boolean backslash = ((c0.y() - c1.y() == 1) && (c1.y() == c2.y()) && (c2.y() - c3.y() == 1))
                || ((c0.y() == c1.y()) && (c1.y() - c2.y() == 1) && (c2.y() == c3.y()));

        return (horizontal && line) || (diagonal && slash) || (diagonal && backslash);
    }
}
