package com.tecsup.petclinic.exceptions;

public class PetTypeNotFoundException extends RuntimeException {

    public PetTypeNotFoundException(String message) {
        super(message);
    }

    public PetTypeNotFoundException(Integer id) {
        super("Tipo de mascota no encontrado con ID: " + id);
    }

    public PetTypeNotFoundException(String name, String type) {
        super("Tipo de mascota no encontrado con nombre: " + name);
    }
}