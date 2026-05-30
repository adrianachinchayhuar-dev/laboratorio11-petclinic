package com.tecsup.petclinic.services;

import java.util.List;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;

public interface OwnerService {

    /**
     *
     * @param ownerDTO
     * @return
     */
    public OwnerDTO create(OwnerDTO ownerDTO);

    /**
     *
     * @param ownerDTO
     * @return
     */
    OwnerDTO update(OwnerDTO ownerDTO);

    /**
     *
     * @param id
     * @throws OwnerNotFoundException
     */
    void delete(Integer id) throws OwnerNotFoundException;

    /**
     *
     * @param id
     * @return
     */
    OwnerDTO findById(Integer id) throws OwnerNotFoundException;

    /**
     *
     * @param firstName
     * @return
     */
    List<OwnerDTO> findByFirstName(String firstName);

    /**
     *
     * @param lastName
     * @return
     */
    List<OwnerDTO> findByLastName(String lastName);

    /**
     *
     * @param city
     * @return
     */
    List<Owner> findByCity(String city);

    /**
     *
     * @return
     */
    List<Owner> findAll();
}