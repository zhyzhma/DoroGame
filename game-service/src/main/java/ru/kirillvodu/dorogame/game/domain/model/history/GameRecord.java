package ru.kirillvodu.dorogame.game.domain.model.history;

import ru.kirillvodu.dorogame.game.domain.model.game.DoroGame;

import java.util.List;

public record GameRecord (DoroGame game, List<ChipMove> chipMoves) { }
