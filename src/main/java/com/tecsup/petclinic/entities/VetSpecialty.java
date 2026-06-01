package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vet_specialties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VetSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @ManyToOne
    @JoinColumn(name = "specialty_id", nullable = false)
    // ✅ CORREGIDO: se llama specialty (no speciality)
    private Speciality specialty;
}