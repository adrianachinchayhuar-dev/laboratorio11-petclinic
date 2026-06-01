package com.tecsup.petclinic.exceptions;

public class VetSpecialtyNotFoundException extends RuntimeException {

    public VetSpecialtyNotFoundException(String message) {
        super(message);
    }

    public VetSpecialtyNotFoundException(Integer vetId, Integer specialtyId) {
        super("Relación no encontrada entre veterinario " + vetId + " y especialidad " + specialtyId);
    }
}