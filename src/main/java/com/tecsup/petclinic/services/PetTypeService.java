package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;

import java.util.List;
import java.util.Optional;

public interface PetTypeService {

    // ==================== MÉTODOS CON DTOs ====================
    PetTypeDTO createPetType(PetTypeDTO petTypeDTO);
    PetTypeDTO updatePetType(Integer id, PetTypeDTO petTypeDTO);
    PetTypeDTO getPetTypeById(Integer id);
    PetTypeDTO getPetTypeByName(String name);
    List<PetTypeDTO> getAllPetTypes();
    List<PetTypeDTO> searchPetTypesByName(String name);
    void deletePetType(Integer id);
    void deletePetTypeByName(String name);

    // ==================== MÉTODOS CON ENTITIES ====================
    PetType save(PetType petType);
    Optional<PetType> findById(Integer id);
    Optional<PetType> findByName(String name);
    List<PetType> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
    boolean existsByName(String name);
}