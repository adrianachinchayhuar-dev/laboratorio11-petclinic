package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.VetSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VetSpecialtyRepository extends JpaRepository<VetSpecialty, Integer> {

    List<VetSpecialty> findByVetId(Integer vetId);

    // ✅ CORREGIDO: SpecialtyId (con ety), no SpecialityId
    List<VetSpecialty> findBySpecialtyId(Integer specialtyId);

    // ✅ CORREGIDO: AndSpecialtyId (con ety)
    Optional<VetSpecialty> findByVetIdAndSpecialtyId(Integer vetId, Integer specialtyId);

    // ✅ CORREGIDO: AndSpecialtyId (con ety)
    boolean existsByVetIdAndSpecialtyId(Integer vetId, Integer specialtyId);

    // ✅ CORREGIDO: AndSpecialtyId (con ety)
    void deleteByVetIdAndSpecialtyId(Integer vetId, Integer specialtyId);
}