package ru.kirillvodu.dorogame.user.application.contracts.DTO.read;

import java.util.UUID;

public record UserReadDTO(UUID id, String name, String avatarKey) {
}
