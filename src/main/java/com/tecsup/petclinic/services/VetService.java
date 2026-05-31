package com.tecsup.petclinic.services;

import java.util.List;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

public interface VetService {

    VetDTO create(VetDTO vetDTO);

    VetDTO update(VetDTO vetDTO);

    void delete(Integer id) throws VetNotFoundException;

    VetDTO findById(Integer id) throws VetNotFoundException;

    List<VetDTO> findByFirstName(String firstName);

    List<VetDTO> findByLastName(String lastName);

    List<Vet> findAll();
}