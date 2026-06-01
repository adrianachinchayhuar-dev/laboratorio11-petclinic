package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Integer> {

    Optional<PetType> findByName(String name);

    List<PetType> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}