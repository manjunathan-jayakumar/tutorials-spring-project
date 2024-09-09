package com.hibernate_concepts.spring_jpa_mapping.exceptions;

public class ResourceNotFoundException extends Exception{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
