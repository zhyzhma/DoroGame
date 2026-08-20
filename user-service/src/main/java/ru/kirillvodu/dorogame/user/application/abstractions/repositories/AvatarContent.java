package ru.kirillvodu.dorogame.user.application.abstractions.repositories;

import java.io.InputStream;

public record AvatarContent(InputStream stream, String contentType, long size) {
}
