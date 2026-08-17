package ru.kirillvodu.dorogame.game.domain.model;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Player {
    private final UserReadModel user;
    private final List<Chip> chips;

    public Player(UserReadModel user, List<Chip> chips) {
        this.user = user;
        this.chips = chips;
    }

    public Chip moveChip(UUID chipId, Coords to) {
        List<Chip> found = chips.stream().filter(x -> x.getId().equals(chipId)).toList();
        if (found.isEmpty()) {
            throw new IllegalArgumentException("Player " + user.name() + " has no chip with id " + chipId);
        }
        found.getFirst().setCoords(to);
        return found.getFirst();
    }

    public boolean isCellOccupied(Coords coords) {
        return chips.stream().anyMatch(c -> c.getCoords().equals(coords));
    }
}
