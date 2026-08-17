package ru.kirillvodu.dorogame.game.domain.model;

import java.util.List;

public record GameRecord (DoroGame game, List<ChipMove> chipMoves) { }
