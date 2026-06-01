package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.Speciality;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialityRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

    private final VetSpecialtyRepository vetSpecialtyRepository;
    private final VetRepository vetRepository;
    private final SpecialityRepository specialityRepository;
    private final VetSpecialtyMapper vetSpecialtyMapper;

    @Override
    public VetSpecialtyDTO assignSpecialtyToVet(Integer vetId, Integer specialtyId) {
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado con ID: " + vetId));

        Speciality specialty = specialityRepository.findById(specialtyId)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + specialtyId));

        // ✅ CORREGIDO: el método se llama existsByVetIdAndSpecialtyId (con Speciality)
        if (vetSpecialtyRepository.existsByVetIdAndSpecialtyId(vetId, specialtyId)) {
            throw new RuntimeException("La especialidad ya está asignada a este veterinario");
        }

        VetSpecialty vetSpecialty = new VetSpecialty();
        vetSpecialty.setVet(vet);
        // ✅ CORREGIDO: el método se llama setSpecialty (no setSpeciality)
        vetSpecialty.setSpecialty(specialty);

        VetSpecialty saved = vetSpecialtyRepository.save(vetSpecialty);
        log.info("Asignada especialidad {} al veterinario {}", specialty.getName(), vet.getFirstName());

        return vetSpecialtyMapper.toDTO(saved);
    }

    @Override
    public List<Speciality> getSpecialtiesByVetId(Integer vetId) {
        return vetSpecialtyRepository.findByVetId(vetId).stream()
                // ✅ CORREGIDO: el método se llama getSpecialty (no getSpeciality)
                .map(VetSpecialty::getSpecialty)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vet> getVetsBySpecialtyId(Integer specialtyId) {
        // ✅ CORREGIDO: el método se llama findBySpecialtyId (no findBySpecialityId)
        return vetSpecialtyRepository.findBySpecialtyId(specialtyId).stream()
                .map(VetSpecialty::getVet)
                .collect(Collectors.toList());
    }

    @Override
    public List<VetSpecialtyDTO> getAllRelations() {
        return vetSpecialtyRepository.findAll().stream()
                .map(vetSpecialtyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isSpecialtyAssignedToVet(Integer vetId, Integer specialtyId) {
        // ✅ CORREGIDO: el método se llama existsByVetIdAndSpecialtyId
        return vetSpecialtyRepository.existsByVetIdAndSpecialtyId(vetId, specialtyId);
    }

    @Override
    public void deleteVetSpecialty(Integer vetId, Integer specialtyId) {
        // ✅ CORREGIDO: el método se llama findByVetIdAndSpecialtyId
        VetSpecialty vetSpecialty = vetSpecialtyRepository
                .findByVetIdAndSpecialtyId(vetId, specialtyId)
                .orElseThrow(() -> new VetSpecialtyNotFoundException(vetId, specialtyId));

        vetSpecialtyRepository.delete(vetSpecialty);
        log.info("Relación eliminada: Veterinario {} - Especialidad {}", vetId, specialtyId);
    }
}