package ru.kirillvodu.dorogame.user.application.abstractions.repositories;

import java.io.InputStream;
import java.util.Optional;

public interface AvatarStorage {
    String uploadAvatar(InputStream in, long size, String contentType);
    Optional<AvatarContent> downloadAvatar(String avatarKey);
}
