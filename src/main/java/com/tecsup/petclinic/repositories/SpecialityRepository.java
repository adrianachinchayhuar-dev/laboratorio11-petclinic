package com.tecsup.petclinic.repositories;

import java.util.List;

import com.tecsup.petclinic.entities.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository
        extends JpaRepository<Speciality, Integer> {

    List<Speciality> findByName(String name);

    @Override
    List<Speciality> findAll();
}