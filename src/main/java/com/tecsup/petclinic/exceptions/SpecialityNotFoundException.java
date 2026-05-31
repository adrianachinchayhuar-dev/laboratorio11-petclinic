package com.tecsup.petclinic.exceptions;

public class SpecialityNotFoundException extends RuntimeException {
    public SpecialityNotFoundException(String message) {
        super(message);
    }
}
