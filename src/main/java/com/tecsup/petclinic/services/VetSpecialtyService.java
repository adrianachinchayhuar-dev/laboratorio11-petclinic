package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.Speciality;
import com.tecsup.petclinic.entities.Vet;

import java.util.List;

public interface VetSpecialtyService {

    VetSpecialtyDTO assignSpecialtyToVet(Integer vetId, Integer specialtyId);

    List<Speciality> getSpecialtiesByVetId(Integer vetId);

    List<Vet> getVetsBySpecialtyId(Integer specialtyId);

    // ✅ NUEVO MÉTODO AGREGADO
    List<VetSpecialtyDTO> getAllRelations();

    boolean isSpecialtyAssignedToVet(Integer vetId, Integer specialtyId);

    void deleteVetSpecialty(Integer vetId, Integer specialtyId);
}