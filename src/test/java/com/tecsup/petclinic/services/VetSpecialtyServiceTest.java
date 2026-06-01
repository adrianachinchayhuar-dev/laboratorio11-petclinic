package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.Speciality;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.SpecialityRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    private Integer testVetId;
    private Integer testSpecialtyId;

    @BeforeEach
    void setUp() {
        // Crear un veterinario de prueba
        Vet vet = new Vet();
        vet.setFirstName("Carlos");
        vet.setLastName("Ramirez");
        Vet savedVet = vetRepository.save(vet);
        testVetId = savedVet.getId();

        // Crear una especialidad de prueba
        Speciality specialty = new Speciality();
        specialty.setName("Radiologia");
        Speciality savedSpecialty = specialityRepository.save(specialty);
        testSpecialtyId = savedSpecialty.getId();
    }

    // ==================== ASIGNACIÓN ====================

    @Test
    void testAssignSpecialtyToVet() {
        VetSpecialtyDTO result = vetSpecialtyService.assignSpecialtyToVet(testVetId, testSpecialtyId);

        assertNotNull(result);
        assertEquals(testVetId, result.getVetId());
        assertEquals(testSpecialtyId, result.getSpecialtyId());
        assertEquals("Carlos", result.getVetFirstName());
        assertEquals("Radiologia", result.getSpecialtyName());
    }

    @Test
    void testAssignSpecialtyAlreadyAssigned() {
        // Primera asignación
        vetSpecialtyService.assignSpecialtyToVet(testVetId, testSpecialtyId);

        // Segunda asignación (debe fallar)
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> vetSpecialtyService.assignSpecialtyToVet(testVetId, testSpecialtyId));
        assertTrue(exception.getMessage().contains("ya está asignada"));
    }

    // ==================== BÚSQUEDA ====================

    @Test
    void testGetSpecialtiesByVetId() {
        // Asignar especialidades
        vetSpecialtyService.assignSpecialtyToVet(testVetId, testSpecialtyId);

        // Crear otra especialidad
        Speciality specialty2 = new Speciality();
        specialty2.setName("Cirugia");
        Speciality savedSpecialty2 = specialityRepository.save(specialty2);
        vetSpecialtyService.assignSpecialtyToVet(testVetId, savedSpecialty2.getId());

        List<Speciality> result = vetSpecialtyService.getSpecialtiesByVetId(testVetId);

        assertThat(result).hasSize(2);
        assertTrue(result.stream().anyMatch(s -> s.getName().equals("Radiologia")));
        assertTrue(result.stream().anyMatch(s -> s.getName().equals("Cirugia")));
    }

    @Test
    void testGetVetsBySpecialtyId() {
        // Asignar especialidad a dos veterinarios
        vetSpecialtyService.assignSpecialtyToVet(testVetId, testSpecialtyId);

        Vet vet2 = new Vet();
        vet2.setFirstName("Maria");
        vet2.setLastName("Lopez");
        Vet savedVet2 = vetRepository.save(vet2);
        vetSpecialtyService.assignSpecialtyToVet(savedVet2.getId(), testSpecialtyId);

        List<Vet> result = vetSpecialtyService.getVetsBySpecialtyId(testSpecialtyId);

        assertThat(result).hasSize(2);
        assertTrue(result.stream().anyMatch(v -> v.getFirstName().equals("Carlos")));
        assertTrue(result.stream().anyMatch(v -> v.getFirstName().equals("Maria")));
    }

    @Test
    void testGetAllRelations() {
        vetSpecialtyService.assignSpecialtyToVet(testVetId, testSpecialtyId);

        List<VetSpecialtyDTO> result = vetSpecialtyService.getAllRelations();

        assertThat(result).isNotEmpty();
        assertTrue(result.stream().anyMatch(r -> r.getVetId().equals(testVetId)));
    }

    // ==================== VERIFICACIÓN ====================

    @Test
    void testIsSpecialtyAssignedToVet() {
        // Antes de asignar
        assertFalse(vetSpecialtyService.isSpecialtyAssignedToVet(testVetId, testSpecialtyId));

        // Después de asignar
        vetSpecialtyService.assignSpecialtyToVet(testVetId, testSpecialtyId);
        assertTrue(vetSpecialtyService.isSpecialtyAssignedToVet(testVetId, testSpecialtyId));
    }

    // ==================== ELIMINACIÓN ====================

    @Test
    void testDeleteVetSpecialty() {
        // Asignar
        vetSpecialtyService.assignSpecialtyToVet(testVetId, testSpecialtyId);

        // Verificar que existe
        assertTrue(vetSpecialtyService.isSpecialtyAssignedToVet(testVetId, testSpecialtyId));

        // Eliminar
        vetSpecialtyService.deleteVetSpecialty(testVetId, testSpecialtyId);

        // Verificar que ya no existe
        assertFalse(vetSpecialtyService.isSpecialtyAssignedToVet(testVetId, testSpecialtyId));
    }

    @Test
    void testDeleteVetSpecialtyNotFound() {
        assertThrows(VetSpecialtyNotFoundException.class,
                () -> vetSpecialtyService.deleteVetSpecialty(999, 999));
    }
}