package com.tecsup.petclinic.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecsup.petclinic.entities.Owner;

@Repository
public interface OwnerRepository
        extends JpaRepository<Owner, Integer> {

    // Fetch owners by firstName
    List<Owner> findByFirstName(String firstName);

    // Fetch owners by lastName
    List<Owner> findByLastName(String lastName);

    // Fetch owners by city
    List<Owner> findByCity(String city);

    @Override
    List<Owner> findAll();

}