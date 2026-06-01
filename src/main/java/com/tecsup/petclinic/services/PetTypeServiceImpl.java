package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.mappers.PetTypeMapper;
import com.tecsup.petclinic.repositories.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PetTypeServiceImpl implements PetTypeService {

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Override
    public PetTypeDTO createPetType(PetTypeDTO petTypeDTO) {
        if (petTypeRepository.existsByName(petTypeDTO.getName())) {
            throw new RuntimeException("Ya existe un tipo de mascota con el nombre: " + petTypeDTO.getName());
        }

        PetType petType = petTypeMapper.toEntity(petTypeDTO);
        PetType saved = petTypeRepository.save(petType);
        return petTypeMapper.toDTO(saved);
    }

    @Override
    public PetTypeDTO updatePetType(Integer id, PetTypeDTO petTypeDTO) {
        PetType existing = petTypeRepository.findById(id)
                .orElseThrow(() -> new PetTypeNotFoundException(id));

        petTypeMapper.updateEntityFromDTO(petTypeDTO, existing);
        PetType updated = petTypeRepository.save(existing);
        return petTypeMapper.toDTO(updated);
    }

    @Override
    public PetTypeDTO getPetTypeById(Integer id) {
        PetType petType = petTypeRepository.findById(id)
                .orElseThrow(() -> new PetTypeNotFoundException(id));
        return petTypeMapper.toDTO(petType);
    }

    @Override
    public PetTypeDTO getPetTypeByName(String name) {
        PetType petType = petTypeRepository.findByName(name)
                .orElseThrow(() -> new PetTypeNotFoundException(name, "name"));
        return petTypeMapper.toDTO(petType);
    }

    @Override
    public List<PetTypeDTO> getAllPetTypes() {
        return petTypeRepository.findAll().stream()
                .map(petTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetTypeDTO> searchPetTypesByName(String name) {
        return petTypeRepository.findByNameContainingIgnoreCase(name).stream()
                .map(petTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePetType(Integer id) {
        if (!petTypeRepository.existsById(id)) {
            throw new PetTypeNotFoundException(id);
        }
        petTypeRepository.deleteById(id);
    }

    @Override
    public void deletePetTypeByName(String name) {
        PetType petType = petTypeRepository.findByName(name)
                .orElseThrow(() -> new PetTypeNotFoundException(name, "name"));
        petTypeRepository.deleteById(petType.getId());
    }

    @Override
    public PetType save(PetType petType) {
        return petTypeRepository.save(petType);
    }

    @Override
    public Optional<PetType> findById(Integer id) {
        return petTypeRepository.findById(id);
    }

    @Override
    public Optional<PetType> findByName(String name) {
        return petTypeRepository.findByName(name);
    }

    @Override
    public List<PetType> findAll() {
        return petTypeRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        petTypeRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return petTypeRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return petTypeRepository.existsByName(name);
    }
}