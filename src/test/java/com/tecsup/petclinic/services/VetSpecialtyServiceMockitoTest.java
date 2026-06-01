package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.Speciality;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialityRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
class VetSpecialtyServiceMockitoTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @MockBean
    private VetSpecialtyRepository vetSpecialtyRepository;

    @MockBean
    private VetRepository vetRepository;

    @MockBean
    private SpecialityRepository specialityRepository;

    @MockBean
    private VetSpecialtyMapper vetSpecialtyMapper;

    // ================================================================
    //  ASIGNACIÓN DE ESPECIALIDADES A VETERINARIOS
    // ================================================================

    /** ✅ ASIGNACIÓN - ÉXITO */
    @Test
    void testAssignSpecialtyToVet() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ASIGNACIÓN DE ESPECIALIDAD (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Vet vet = new Vet(1, "Carlos", "Ramirez");
        Speciality specialty = new Speciality(1, "Radiologia");
        VetSpecialty vetSpecialty = new VetSpecialty(1, vet, specialty);
        VetSpecialtyDTO outputDTO = new VetSpecialtyDTO(1, 1, "Carlos", "Ramirez", 1, "Radiologia");

        when(vetRepository.findById(1)).thenReturn(Optional.of(vet));
        when(specialityRepository.findById(1)).thenReturn(Optional.of(specialty));
        when(vetSpecialtyRepository.existsByVetIdAndSpecialtyId(1, 1)).thenReturn(false);
        when(vetSpecialtyRepository.save(any(VetSpecialty.class))).thenReturn(vetSpecialty);
        when(vetSpecialtyMapper.toDTO(any(VetSpecialty.class))).thenReturn(outputDTO);

        VetSpecialtyDTO result = vetSpecialtyService.assignSpecialtyToVet(1, 1);

        assertNotNull(result, "❌ ERROR: El resultado no debería ser null");
        assertEquals(1, result.getVetId(), "❌ ERROR: VetId esperado=1, actual=" + result.getVetId());
        assertEquals(1, result.getSpecialtyId(), "❌ ERROR: SpecialtyId esperado=1, actual=" + result.getSpecialtyId());

        log.info("✅ RESULTADO: Asignación exitosa → VetID={}, SpecialtyID={}", result.getVetId(), result.getSpecialtyId());
    }

    /** ❌ ASIGNACIÓN - ERROR: veterinario no existe */
    @Test
    void testAssignSpecialtyToVet_Error_VetNotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ASIGNACIÓN DE ESPECIALIDAD (ERROR: VET NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        when(vetRepository.findById(999)).thenReturn(Optional.empty());

        log.info("🎯 Esperado: Lanzar RuntimeException con mensaje 'Veterinario no encontrado'");

        Exception exception = assertThrows(RuntimeException.class,
                () -> vetSpecialtyService.assignSpecialtyToVet(999, 1));

        String actualMessage = exception.getMessage();
        log.info("✅ Actual: Se lanzó RuntimeException con mensaje: '{}'", actualMessage);

        assertTrue(actualMessage.contains("Veterinario no encontrado"),
                "❌ ERROR: El mensaje debería contener 'Veterinario no encontrado' pero fue: " + actualMessage);
        log.info("✅ RESULTADO: Prueba de error superada ✓");
    }

    /** ❌ ASIGNACIÓN - ERROR: especialidad no existe */
    @Test
    void testAssignSpecialtyToVet_Error_SpecialtyNotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ASIGNACIÓN DE ESPECIALIDAD (ERROR: ESPECIALIDAD NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Vet vet = new Vet(1, "Carlos", "Ramirez");
        when(vetRepository.findById(1)).thenReturn(Optional.of(vet));
        when(specialityRepository.findById(999)).thenReturn(Optional.empty());

        log.info("🎯 Esperado: Lanzar RuntimeException con mensaje 'Especialidad no encontrada'");

        Exception exception = assertThrows(RuntimeException.class,
                () -> vetSpecialtyService.assignSpecialtyToVet(1, 999));

        String actualMessage = exception.getMessage();
        log.info("✅ Actual: Se lanzó RuntimeException con mensaje: '{}'", actualMessage);

        assertTrue(actualMessage.contains("Especialidad no encontrada"),
                "❌ ERROR: El mensaje debería contener 'Especialidad no encontrada' pero fue: " + actualMessage);
        log.info("✅ RESULTADO: Prueba de error superada ✓");
    }

    /** ❌ ASIGNACIÓN - ERROR: especialidad ya asignada */
    @Test
    void testAssignSpecialtyToVet_Error_AlreadyAssigned() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ASIGNACIÓN DE ESPECIALIDAD (ERROR: YA ASIGNADA)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Vet vet = new Vet(1, "Carlos", "Ramirez");
        Speciality specialty = new Speciality(1, "Radiologia");

        when(vetRepository.findById(1)).thenReturn(Optional.of(vet));
        when(specialityRepository.findById(1)).thenReturn(Optional.of(specialty));
        when(vetSpecialtyRepository.existsByVetIdAndSpecialtyId(1, 1)).thenReturn(true);

        log.info("🎯 Esperado: Lanzar RuntimeException con mensaje 'ya está asignada'");

        Exception exception = assertThrows(RuntimeException.class,
                () -> vetSpecialtyService.assignSpecialtyToVet(1, 1));

        String actualMessage = exception.getMessage();
        log.info("✅ Actual: Se lanzó RuntimeException con mensaje: '{}'", actualMessage);

        assertTrue(actualMessage.contains("ya está asignada"),
                "❌ ERROR: El mensaje debería contener 'ya está asignada' pero fue: " + actualMessage);
        log.info("✅ RESULTADO: Prueba de error superada ✓");
    }

    // ================================================================
    //  BÚSQUEDA DE ESPECIALIDADES POR VETERINARIO
    // ================================================================

    /** ✅ BÚSQUEDA ESPECIALIDADES POR VET - ÉXITO */
    @Test
    void testGetSpecialtiesByVetId() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BÚSQUEDA DE ESPECIALIDADES POR VETERINARIO (ÉXITO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Vet vet = new Vet(1, "Carlos", "Ramirez");
        Speciality spec1 = new Speciality(1, "Radiologia");
        Speciality spec2 = new Speciality(2, "Cirugia");

        VetSpecialty vs1 = new VetSpecialty(1, vet, spec1);
        VetSpecialty vs2 = new VetSpecialty(2, vet, spec2);

        when(vetSpecialtyRepository.findByVetId(1)).thenReturn(Arrays.asList(vs1, vs2));

        List<Speciality> result = vetSpecialtyService.getSpecialtiesByVetId(1);

        int expectedSize = 2;
        int actualSize = result.size();

        log.info("🎯 Esperado: {} especialidades", expectedSize);
        log.info("📊 Actual: {} especialidades", actualSize);

        assertEquals(expectedSize, actualSize,
                "❌ ERROR: Se esperaban " + expectedSize + " especialidades pero se encontraron " + actualSize);

        assertTrue(result.stream().anyMatch(s -> s.getName().equals("Radiologia")),
                "❌ ERROR: No se encontró la especialidad 'Radiologia'");
        assertTrue(result.stream().anyMatch(s -> s.getName().equals("Cirugia")),
                "❌ ERROR: No se encontró la especialidad 'Cirugia'");

        log.info("✅ RESULTADO: Especialidades encontradas: {}",
                result.stream().map(Speciality::getName).toList());
    }

    /** ❌ BÚSQUEDA - ERROR: sin resultados */
    @Test
    void testGetSpecialtiesByVetId_Error_NoResults() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BÚSQUEDA DE ESPECIALIDADES (ERROR: SIN RESULTADOS)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        when(vetSpecialtyRepository.findByVetId(999)).thenReturn(Arrays.asList());

        List<Speciality> result = vetSpecialtyService.getSpecialtiesByVetId(999);

        int expectedSize = 0;
        int actualSize = result.size();

        log.info("🎯 Esperado: {} especialidades (lista vacía)", expectedSize);
        log.info("📊 Actual: {} especialidades", actualSize);

        assertEquals(expectedSize, actualSize,
                "❌ ERROR: Para vet sin especialidades se esperaban " + expectedSize + " pero se encontraron " + actualSize);

        log.info("✅ RESULTADO: Lista vacía retornada correctamente ✓");
    }

    // ================================================================
    //  BÚSQUEDA DE VETERINARIOS POR ESPECIALIDAD
    // ================================================================

    /** ✅ BÚSQUEDA VETS POR ESPECIALIDAD - ÉXITO */
    @Test
    void testGetVetsBySpecialtyId() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BÚSQUEDA DE VETERINARIOS POR ESPECIALIDAD (ÉXITO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Speciality specialty = new Speciality(1, "Radiologia");
        Vet vet1 = new Vet(1, "Carlos", "Ramirez");
        Vet vet2 = new Vet(2, "Maria", "Lopez");

        VetSpecialty vs1 = new VetSpecialty(1, vet1, specialty);
        VetSpecialty vs2 = new VetSpecialty(2, vet2, specialty);

        when(vetSpecialtyRepository.findBySpecialtyId(1)).thenReturn(Arrays.asList(vs1, vs2));

        List<Vet> result = vetSpecialtyService.getVetsBySpecialtyId(1);

        int expectedSize = 2;
        int actualSize = result.size();

        log.info("🎯 Esperado: {} veterinarios", expectedSize);
        log.info("📊 Actual: {} veterinarios", actualSize);

        assertEquals(expectedSize, actualSize,
                "❌ ERROR: Se esperaban " + expectedSize + " veterinarios pero se encontraron " + actualSize);

        assertTrue(result.stream().anyMatch(v -> v.getFirstName().equals("Carlos")),
                "❌ ERROR: No se encontró al veterinario 'Carlos'");
        assertTrue(result.stream().anyMatch(v -> v.getFirstName().equals("Maria")),
                "❌ ERROR: No se encontró a la veterinaria 'Maria'");

        log.info("✅ RESULTADO: Veterinarios encontrados: {}",
                result.stream().map(v -> v.getFirstName()).toList());
    }
    /** ❌ BÚSQUEDA VETS POR ESPECIALIDAD - ERROR: lista vacía (DEBE FALLAR - X ROJA) */
    @Test
    void testGetVetsBySpecialtyId_Error_NoResults() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BÚSQUEDA DE VETERINARIOS POR ESPECIALIDAD (ERROR: SIN RESULTADOS)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer nonExistentSpecialtyId = 999;

        log.info("🎯 Esperado: 2 veterinarios para la especialidad ID={}", nonExistentSpecialtyId);
        log.info("⚠️  PERO el mock devuelve una lista VACÍA");
        log.info("🔴 Esto hará que la prueba FALLE con X ROJA porque assertEquals(2, 0) fallará");

        // ⚠️ MOCK INCORRECTO A PROPÓSITO: devuelve lista vacía
        when(vetSpecialtyRepository.findBySpecialtyId(nonExistentSpecialtyId))
                .thenReturn(Arrays.asList());

        List<Vet> result = vetSpecialtyService.getVetsBySpecialtyId(nonExistentSpecialtyId);

        int expectedSize = 2;
        int actualSize = result.size();

        log.info("🎯 Esperado: {} veterinarios", expectedSize);
        log.info("📊 Actual: {} veterinarios", actualSize);

        // Esta prueba FALLARÁ porque espera 2 pero recibe 0
        assertEquals(expectedSize, actualSize,
                "❌ ERROR: Se esperaban " + expectedSize + " veterinarios pero se encontraron " + actualSize);

        log.info("🔴 RESULTADO: La prueba falló (X ROJA) porque se esperaban {} veterinarios pero se encontraron {}",
                expectedSize, actualSize);
    }

    // ================================================================
    //  VERIFICACIÓN
    // ================================================================

    /** ✅ VERIFICACIÓN - ÉXITO */
    @Test
    void testIsSpecialtyAssignedToVet() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: VERIFICACIÓN DE ESPECIALIDAD ASIGNADA (ÉXITO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        when(vetSpecialtyRepository.existsByVetIdAndSpecialtyId(1, 1)).thenReturn(true);
        when(vetSpecialtyRepository.existsByVetIdAndSpecialtyId(1, 999)).thenReturn(false);

        boolean assigned = vetSpecialtyService.isSpecialtyAssignedToVet(1, 1);
        boolean notAssigned = vetSpecialtyService.isSpecialtyAssignedToVet(1, 999);

        log.info("🎯 Esperado: assigned = true");
        log.info("📊 Actual: assigned = {}", assigned);
        assertTrue(assigned, "❌ ERROR: La especialidad debería estar asignada");

        log.info("🎯 Esperado: notAssigned = false");
        log.info("📊 Actual: notAssigned = {}", notAssigned);
        assertFalse(notAssigned, "❌ ERROR: La especialidad NO debería estar asignada");

        log.info("✅ RESULTADO: Verificaciones correctas ✓");
    }

    // ================================================================
    //  ELIMINACIÓN DE RELACIÓN
    // ================================================================

    /** ✅ ELIMINACIÓN - ÉXITO */
    @Test
    void testDeleteVetSpecialty() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ELIMINACIÓN DE RELACIÓN (ÉXITO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Vet vet = new Vet(1, "Carlos", "Ramirez");
        Speciality specialty = new Speciality(1, "Radiologia");
        VetSpecialty vetSpecialty = new VetSpecialty(1, vet, specialty);

        when(vetSpecialtyRepository.findByVetIdAndSpecialtyId(1, 1))
                .thenReturn(Optional.of(vetSpecialty));
        doNothing().when(vetSpecialtyRepository).delete(vetSpecialty);

        vetSpecialtyService.deleteVetSpecialty(1, 1);

        log.info("✅ RESULTADO: Relación eliminada correctamente");
        verify(vetSpecialtyRepository, times(1)).delete(vetSpecialty);
    }

    /** ❌ ELIMINACIÓN - ERROR: relación no existe */
    @Test
    void testDeleteVetSpecialty_Error_NotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ELIMINACIÓN DE RELACIÓN (ERROR: RELACIÓN NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        when(vetSpecialtyRepository.findByVetIdAndSpecialtyId(999, 999))
                .thenReturn(Optional.empty());

        log.info("🎯 Esperado: Lanzar VetSpecialtyNotFoundException");

        Exception exception = assertThrows(VetSpecialtyNotFoundException.class,
                () -> vetSpecialtyService.deleteVetSpecialty(999, 999));

        String actualMessage = exception.getMessage();
        log.info("✅ Actual: Se lanzó VetSpecialtyNotFoundException con mensaje: '{}'", actualMessage);

        log.info("✅ RESULTADO: Prueba de error superada ✓");
    }

    // ================================================================
    //  OBTENER TODAS LAS RELACIONES
    // ================================================================

    /** ✅ GET ALL - ÉXITO */
    @Test
    void testGetAllRelations() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: OBTENER TODAS LAS RELACIONES (ÉXITO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Vet vet = new Vet(1, "Carlos", "Ramirez");
        Speciality spec1 = new Speciality(1, "Radiologia");
        Speciality spec2 = new Speciality(2, "Cirugia");

        VetSpecialty vs1 = new VetSpecialty(1, vet, spec1);
        VetSpecialty vs2 = new VetSpecialty(2, vet, spec2);

        VetSpecialtyDTO dto1 = new VetSpecialtyDTO(1, 1, "Carlos", "Ramirez", 1, "Radiologia");
        VetSpecialtyDTO dto2 = new VetSpecialtyDTO(2, 1, "Carlos", "Ramirez", 2, "Cirugia");

        when(vetSpecialtyRepository.findAll()).thenReturn(Arrays.asList(vs1, vs2));
        when(vetSpecialtyMapper.toDTO(vs1)).thenReturn(dto1);
        when(vetSpecialtyMapper.toDTO(vs2)).thenReturn(dto2);

        List<VetSpecialtyDTO> result = vetSpecialtyService.getAllRelations();

        int expectedSize = 2;
        int actualSize = result.size();

        log.info("🎯 Esperado: {} relaciones", expectedSize);
        log.info("📊 Actual: {} relaciones", actualSize);

        assertEquals(expectedSize, actualSize,
                "❌ ERROR: Se esperaban " + expectedSize + " relaciones pero se encontraron " + actualSize);

        log.info("✅ RESULTADO: Relaciones encontradas: {}", result.size());
    }
}