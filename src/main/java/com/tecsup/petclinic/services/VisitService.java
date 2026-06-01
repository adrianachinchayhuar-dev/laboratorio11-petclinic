package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.PetNotFoundException;

import java.util.List;
import java.util.Optional;

public interface VisitService {

    // ===== MÉTODOS CON DTOs =====
    VisitDTO createVisit(VisitDTO visitDTO) throws PetNotFoundException;
    VisitDTO updateVisit(Integer id, VisitDTO visitDTO) throws PetNotFoundException;
    VisitDTO getVisitById(Integer id);
    List<VisitDTO> getAllVisits();
    List<VisitDTO> getVisitsByPetId(Integer petId) throws PetNotFoundException;
    void deleteVisit(Integer id);
    void deleteVisitsByPetId(Integer petId) throws PetNotFoundException;

    // ===== MÉTODOS CON ENTITIES =====
    Visit save(Visit visit);
    Optional<Visit> findById(Integer id);
    List<Visit> findAll();
    List<Visit> findByPetIdEntity(Integer petId);
    void deleteById(Integer id);
    void deleteByPetId(Integer petId);
    boolean existsById(Integer id);
}