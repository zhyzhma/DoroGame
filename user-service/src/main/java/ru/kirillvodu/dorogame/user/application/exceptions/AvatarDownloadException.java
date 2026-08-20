package ru.kirillvodu.dorogame.user.application.exceptions;

public class AvatarDownloadException extends RuntimeException {
    public AvatarDownloadException(String message) {
        super("Couldn't download avatar. Reason: " + message);
    }
}
