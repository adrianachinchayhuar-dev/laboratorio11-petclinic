package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.PetNotFoundException;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.mappers.VisitMapper;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VisitMapper visitMapper;

    // ==================== MÉTODOS CON DTOs ====================

    @Override
    public VisitDTO createVisit(VisitDTO visitDTO) throws PetNotFoundException {
        // Obtener la mascota
        Pet pet = petRepository.findById(visitDTO.getPetId())
                .orElseThrow(() -> new PetNotFoundException("Mascota no encontrada con ID: " + visitDTO.getPetId()));

        // Crear la visita
        Visit visit = new Visit();
        visit.setDate(visitDTO.getDate() != null ? visitDTO.getDate() : LocalDate.now());
        visit.setDescription(visitDTO.getDescription());
        visit.setPet(pet);

        // Guardar
        Visit savedVisit = visitRepository.save(visit);

        // Retornar DTO
        return visitMapper.toDTO(savedVisit);
    }

    @Override
    public VisitDTO updateVisit(Integer id, VisitDTO visitDTO) throws PetNotFoundException {
        // Buscar visita existente
        Visit existingVisit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visita no encontrada con ID: " + id));

        // Actualizar fecha
        if (visitDTO.getDate() != null) {
            existingVisit.setDate(visitDTO.getDate());
        }

        // Actualizar descripción
        if (visitDTO.getDescription() != null) {
            existingVisit.setDescription(visitDTO.getDescription());
        }

        // Actualizar mascota si es necesario
        if (visitDTO.getPetId() != null && !visitDTO.getPetId().equals(existingVisit.getPet().getId())) {
            Pet pet = petRepository.findById(visitDTO.getPetId())
                    .orElseThrow(() -> new PetNotFoundException("Mascota no encontrada con ID: " + visitDTO.getPetId()));
            existingVisit.setPet(pet);
        }

        // Guardar cambios
        Visit updatedVisit = visitRepository.save(existingVisit);

        return visitMapper.toDTO(updatedVisit);
    }

    @Override
    public VisitDTO getVisitById(Integer id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visita no encontrada con ID: " + id));
        return visitMapper.toDTO(visit);
    }

    @Override
    public List<VisitDTO> getAllVisits() {
        return visitRepository.findAll()
                .stream()
                .map(visitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitDTO> getVisitsByPetId(Integer petId) throws PetNotFoundException {
        // Verificar que la mascota existe
        if (!petRepository.existsById(petId)) {
            throw new PetNotFoundException("Mascota no encontrada con ID: " + petId);
        }

        return visitRepository.findByPetId(petId)
                .stream()
                .map(visitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteVisit(Integer id) {
        if (!visitRepository.existsById(id)) {
            throw new VisitNotFoundException("Visita no encontrada con ID: " + id);
        }
        visitRepository.deleteById(id);
    }

    @Override
    public void deleteVisitsByPetId(Integer petId) throws PetNotFoundException {
        if (!petRepository.existsById(petId)) {
            throw new PetNotFoundException("Mascota no encontrada con ID: " + petId);
        }
        visitRepository.deleteByPetId(petId);
    }

    // ==================== MÉTODOS CON ENTITIES ====================

    @Override
    public Visit save(Visit visit) {
        if (visit.getDate() == null) {
            visit.setDate(LocalDate.now());
        }
        return visitRepository.save(visit);
    }

    @Override
    public Optional<Visit> findById(Integer id) {
        return visitRepository.findById(id);
    }

    @Override
    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    @Override
    public List<Visit> findByPetIdEntity(Integer petId) {
        return visitRepository.findByPetId(petId);
    }

    @Override
    public void deleteById(Integer id) {
        visitRepository.deleteById(id);
    }

    @Override
    public void deleteByPetId(Integer petId) {
        visitRepository.deleteByPetId(petId);
    }

    @Override
    public boolean existsById(Integer id) {
        return visitRepository.existsById(id);
    }
}