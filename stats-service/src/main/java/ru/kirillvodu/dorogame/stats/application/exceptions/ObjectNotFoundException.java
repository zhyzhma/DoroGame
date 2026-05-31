package ru.kirillvodu.dorogame.stats.application.exceptions;

import java.util.UUID;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(UUID id, String entityName) {
        super(entityName + " not found: " + id);
    }
}
