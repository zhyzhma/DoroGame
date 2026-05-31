package ru.kirillvodu.dorogame.stats.application.contracts.events;

public record GameFinishedEvent(String gameId, String winnerId, String loserId) {
}
