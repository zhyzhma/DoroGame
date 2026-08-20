package ru.kirillvodu.dorogame.user.application.exceptions;

public class AvatarUploadException extends RuntimeException {
    public AvatarUploadException(String message) {
        super("Couldn't upload avatar. Reason: " + message);
    }
}
