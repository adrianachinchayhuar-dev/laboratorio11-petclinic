package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {  // ← Integer

    List<Visit> findByPetId(Integer petId);  // ← Integer

    void deleteByPetId(Integer petId);  // ← Integer
}