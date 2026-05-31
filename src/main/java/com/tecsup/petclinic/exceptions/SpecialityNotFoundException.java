package com.tecsup.petclinic.exceptions;

public class SpecialityNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public SpecialityNotFoundException(String message) {
        super(message);
    }
}