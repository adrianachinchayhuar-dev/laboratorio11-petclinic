package com.tecsup.petclinic.services;

import java.util.List;

import com.tecsup.petclinic.dtos.SpecialityDTO;
import com.tecsup.petclinic.entities.Speciality;
import com.tecsup.petclinic.exceptions.SpecialityNotFoundException;

public interface SpecialityService {

    SpecialityDTO create(SpecialityDTO specialityDTO);

    SpecialityDTO update(SpecialityDTO specialityDTO);

    void delete(Integer id) throws SpecialityNotFoundException;

    SpecialityDTO findById(Integer id)
            throws SpecialityNotFoundException;

    List<SpecialityDTO> findByName(String name);

    List<Speciality> findAll();
}