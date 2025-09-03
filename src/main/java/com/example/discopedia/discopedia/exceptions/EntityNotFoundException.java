package com.example.discopedia.discopedia.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityClass, String attributeName, String attributeValue) {
        super(String.format("%s with %s \"%s\" not found", entityClass, attributeName, attributeValue));
    }
}
