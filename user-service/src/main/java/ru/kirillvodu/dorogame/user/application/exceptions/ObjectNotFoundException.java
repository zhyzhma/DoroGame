package ru.kirillvodu.dorogame.user.application.exceptions;

import java.util.UUID;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(UUID id, String entityName) {
        super(entityName + " not found: " + id);
    }
}
