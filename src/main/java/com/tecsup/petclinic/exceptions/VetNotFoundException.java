package com.tecsup.petclinic.exceptions;

public class VetNotFoundException extends RuntimeException {
    public VetNotFoundException(String message) {
        super(message);
    }
}
