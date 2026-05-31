package com.tecsup.petclinic.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tecsup.petclinic.dtos.SpecialityDTO;
import com.tecsup.petclinic.entities.Speciality;
import com.tecsup.petclinic.exceptions.SpecialityNotFoundException;
import com.tecsup.petclinic.mappers.SpecialityMapper;
import com.tecsup.petclinic.repositories.SpecialityRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpecialityServiceImpl
        implements SpecialityService {

    private final SpecialityRepository specialityRepository;
    private final SpecialityMapper specialityMapper;

    public SpecialityServiceImpl(
            SpecialityRepository specialityRepository,
            SpecialityMapper specialityMapper
    ) {
        this.specialityRepository = specialityRepository;
        this.specialityMapper = specialityMapper;
    }

    @Override
    public SpecialityDTO create(
            SpecialityDTO specialityDTO
    ) {

        Speciality newSpeciality =
                specialityRepository.save(
                        specialityMapper.mapToEntity(specialityDTO)
                );

        return specialityMapper.mapToDto(newSpeciality);
    }

    @Override
    public SpecialityDTO update(
            SpecialityDTO specialityDTO
    ) {

        Speciality newSpeciality =
                specialityRepository.save(
                        specialityMapper.mapToEntity(specialityDTO)
                );

        return specialityMapper.mapToDto(newSpeciality);
    }

    @Override
    public void delete(Integer id)
            throws SpecialityNotFoundException {

        SpecialityDTO speciality = findById(id);

        specialityRepository.delete(
                specialityMapper.mapToEntity(speciality)
        );
    }

    @Override
    public SpecialityDTO findById(Integer id)
            throws SpecialityNotFoundException {

        Optional<Speciality> speciality =
                specialityRepository.findById(id);

        if (!speciality.isPresent()) {
            throw new SpecialityNotFoundException(
                    "Record not found...!"
            );
        }

        return specialityMapper.mapToDto(speciality.get());
    }

    @Override
    public List<SpecialityDTO> findByName(String name) {

        List<Speciality> specialities =
                specialityRepository.findByName(name);

        specialities.forEach(
                speciality -> log.info("" + speciality)
        );

        return specialities
                .stream()
                .map(specialityMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Speciality> findAll() {

        return specialityRepository.findAll();
    }
}