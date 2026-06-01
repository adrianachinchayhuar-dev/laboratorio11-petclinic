package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.PetNotFoundException;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.mappers.VisitMapper;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
class VisitServiceMockitoTest {

    @Autowired
    private VisitService visitService;

    @MockBean
    private VisitRepository visitRepository;

    @MockBean
    private PetRepository petRepository;

    @MockBean
    private VisitMapper visitMapper;

    // ================================================================
    //  CREATE - PRUEBAS DE CREACIÓN
    // ================================================================

    /** ✅ CREATE - ÉXITO (CHECK VERDE) */
    @Test
    void testCreateVisit() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: CREAR VISITA (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Pet pet = new Pet();
        pet.setId(1);

        Visit saved = new Visit();
        saved.setId(1);
        saved.setDescription("Chequeo general");
        saved.setDate(LocalDate.now());
        saved.setPet(pet);

        VisitDTO input = new VisitDTO();
        input.setDescription("Chequeo general");
        input.setPetId(1);

        VisitDTO output = new VisitDTO();
        output.setId(1);
        output.setDescription("Chequeo general");
        output.setPetId(1);

        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        when(visitRepository.save(any(Visit.class))).thenReturn(saved);
        when(visitMapper.toDTO(any(Visit.class))).thenReturn(output);

        VisitDTO result = visitService.createVisit(input);

        log.info("🎯 Esperado: ID no null, Descripción='Chequeo general'");
        log.info("📊 Actual: ID={}, Descripción='{}'", result.getId(), result.getDescription());

        assertNotNull(result.getId(), "❌ ERROR: El ID no debería ser null");
        assertEquals("Chequeo general", result.getDescription(),
                "❌ ERROR: Se esperaba 'Chequeo general' pero fue '" + result.getDescription() + "'");

        log.info("✅ RESULTADO: Visita creada correctamente con ID={}", result.getId());
    }

    /** ❌ CREATE - ERROR: mascota no existe (DEBE FALLAR - X ROJA) */
    @Test
    void testCreateVisit_Error_PetNotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: CREAR VISITA (ERROR: MASCOTA NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        VisitDTO input = new VisitDTO();
        input.setDescription("Visita invalida");
        input.setPetId(999);

        log.info("🎯 Esperado: Lanzar PetNotFoundException");
        log.info("⚠️  Configuración: petRepository.findById(999) → Optional.empty()");

        // ✅ CORRECTO: Mock devuelve Optional.empty() para que el servicio lance excepción
        when(petRepository.findById(999)).thenReturn(Optional.empty());

        try {
            visitService.createVisit(input);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado PetNotFoundException pero no se lanzó ninguna excepción");
        } catch (PetNotFoundException e) {
            log.info("✅ Actual: Se lanzó PetNotFoundException con mensaje: '{}'", e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
            assertTrue(e.getMessage().contains("Mascota no encontrada"),
                    "❌ El mensaje debería contener 'Mascota no encontrada'");
        }
    }

    // ================================================================
    //  FIND - PRUEBAS DE BÚSQUEDA
    // ================================================================

    /** ✅ FIND - ÉXITO (CHECK VERDE) */
    @Test
    void testFindVisitById() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BUSCAR VISITA POR ID (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Pet pet = new Pet();
        pet.setId(1);

        Visit visit = new Visit();
        visit.setId(1);
        visit.setDescription("Consulta");
        visit.setPet(pet);

        VisitDTO dto = new VisitDTO();
        dto.setId(1);
        dto.setDescription("Consulta");
        dto.setPetId(1);

        when(visitRepository.findById(1)).thenReturn(Optional.of(visit));
        when(visitMapper.toDTO(visit)).thenReturn(dto);

        VisitDTO result = visitService.getVisitById(1);

        log.info("🎯 Esperado: ID=1, Descripción='Consulta'");
        log.info("📊 Actual: ID={}, Descripción='{}'", result.getId(), result.getDescription());

        assertEquals(1, result.getId(), "❌ ERROR: Se esperaba ID=1 pero fue ID=" + result.getId());
        assertEquals("Consulta", result.getDescription(),
                "❌ ERROR: Se esperaba 'Consulta' pero fue '" + result.getDescription() + "'");

        log.info("✅ RESULTADO: Visita encontrada correctamente");
    }

    /** ❌ FIND - ERROR: visita no existe (DEBE FALLAR - X ROJA) */
    @Test
    void testFindVisitById_Error_NotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BUSCAR VISITA POR ID (ERROR: VISITA NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer nonExistentId = 999;
        log.info("🎯 Esperado: Lanzar VisitNotFoundException para ID={}", nonExistentId);
        log.info("⚠️  Configuración: visitRepository.findById({}) → Optional.empty()", nonExistentId);

        // ✅ CORRECTO: Mock devuelve Optional.empty()
        when(visitRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        try {
            visitService.getVisitById(nonExistentId);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado VisitNotFoundException pero no se lanzó ninguna excepción");
        } catch (VisitNotFoundException e) {
            log.info("✅ Actual: Se lanzó VisitNotFoundException con mensaje: '{}'", e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
        }
    }

    // ================================================================
    //  UPDATE - PRUEBAS DE ACTUALIZACIÓN
    // ================================================================

    /** ✅ UPDATE - ÉXITO (CHECK VERDE) */
    @Test
    void testUpdateVisit() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ACTUALIZAR VISITA (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Pet pet = new Pet();
        pet.setId(1);

        Visit visit = new Visit();
        visit.setId(1);
        visit.setDescription("Antiguo");
        visit.setPet(pet);

        VisitDTO updateDTO = new VisitDTO();
        updateDTO.setDescription("Actualizado");

        Visit updated = new Visit();
        updated.setId(1);
        updated.setDescription("Actualizado");
        updated.setPet(pet);

        VisitDTO resultDTO = new VisitDTO();
        resultDTO.setId(1);
        resultDTO.setDescription("Actualizado");

        when(visitRepository.findById(1)).thenReturn(Optional.of(visit));
        when(visitRepository.save(any(Visit.class))).thenReturn(updated);
        when(visitMapper.toDTO(any(Visit.class))).thenReturn(resultDTO);

        VisitDTO result = visitService.updateVisit(1, updateDTO);

        log.info("🎯 Esperado: Descripción='Actualizado'");
        log.info("📊 Actual: Descripción='{}'", result.getDescription());

        assertEquals("Actualizado", result.getDescription(),
                "❌ ERROR: Se esperaba 'Actualizado' pero fue '" + result.getDescription() + "'");

        log.info("✅ RESULTADO: Visita actualizada correctamente");
    }

    /** ❌ UPDATE - ERROR: visita no existe (DEBE FALLAR - X ROJA) */
    @Test
    void testUpdateVisit_Error_NotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ACTUALIZAR VISITA (ERROR: VISITA NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer nonExistentId = 999;
        VisitDTO updateDTO = new VisitDTO();
        updateDTO.setDescription("Actualizacion invalida");

        log.info("🎯 Esperado: Lanzar VisitNotFoundException para ID={}", nonExistentId);
        log.info("⚠️  Configuración: visitRepository.findById({}) → Optional.empty()", nonExistentId);

        // ✅ CORRECTO: Mock devuelve Optional.empty()
        when(visitRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        try {
            visitService.updateVisit(nonExistentId, updateDTO);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado VisitNotFoundException pero no se lanzó ninguna excepción");
        } catch (VisitNotFoundException e) {
            log.info("✅ Actual: Se lanzó VisitNotFoundException con mensaje: '{}'", e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
        }
    }

    // ================================================================
    //  DELETE - PRUEBAS DE ELIMINACIÓN
    // ================================================================

    /** ✅ DELETE - ÉXITO (CHECK VERDE) */
    @Test
    void testDeleteVisit() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ELIMINAR VISITA (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer visitId = 1;
        log.info("🎯 Esperado: Eliminar visita con ID={} sin excepciones", visitId);
        log.info("⚠️  Configuración: visitRepository.existsById({}) → true", visitId);

        when(visitRepository.existsById(visitId)).thenReturn(true);
        doNothing().when(visitRepository).deleteById(visitId);

        assertDoesNotThrow(() -> visitService.deleteVisit(visitId),
                "❌ ERROR: No debería lanzar excepción al eliminar");

        verify(visitRepository, times(1)).deleteById(visitId);
        log.info("✅ RESULTADO: Visita eliminada correctamente");
    }

    /** ❌ DELETE - ERROR: visita no existe (DEBE FALLAR - X ROJA) */
    @Test
    void testDeleteVisit_Error_NotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ELIMINAR VISITA (ERROR: VISITA NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer nonExistentId = 999;
        log.info("🎯 Esperado: Lanzar VisitNotFoundException para ID={}", nonExistentId);
        log.info("⚠️  Configuración: visitRepository.existsById({}) → false", nonExistentId);

        // ✅ CORRECTO: Mock devuelve false para que el servicio lance excepción
        when(visitRepository.existsById(nonExistentId)).thenReturn(false);

        try {
            visitService.deleteVisit(nonExistentId);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado VisitNotFoundException pero no se lanzó ninguna excepción");
        } catch (VisitNotFoundException e) {
            log.info("✅ Actual: Se lanzó VisitNotFoundException con mensaje: '{}'", e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
        }
    }
}