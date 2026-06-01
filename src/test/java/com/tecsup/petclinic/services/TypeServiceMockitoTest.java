package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.mappers.PetTypeMapper;
import com.tecsup.petclinic.repositories.PetTypeRepository;
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
class TypeServiceMockitoTest {

    @Autowired
    private PetTypeService petTypeService;

    @MockBean
    private PetTypeRepository petTypeRepository;

    @MockBean
    private PetTypeMapper petTypeMapper;

    // ================================================================
    //  CREATE - PRUEBAS DE CREACIÓN
    // ================================================================

    /** ✅ CREATE - ÉXITO (CHECK VERDE) */
    @Test
    void testCreatePetType() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: CREAR TIPO DE MASCOTA (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        PetTypeDTO inputDTO = new PetTypeDTO();
        inputDTO.setName("Perro");

        PetType petType = new PetType();
        petType.setName("Perro");

        PetType savedPetType = new PetType();
        savedPetType.setId(1);
        savedPetType.setName("Perro");

        PetTypeDTO outputDTO = new PetTypeDTO();
        outputDTO.setId(1);
        outputDTO.setName("Perro");

        when(petTypeRepository.existsByName("Perro")).thenReturn(false);
        when(petTypeMapper.toEntity(any(PetTypeDTO.class))).thenReturn(petType);
        when(petTypeRepository.save(any(PetType.class))).thenReturn(savedPetType);
        when(petTypeMapper.toDTO(any(PetType.class))).thenReturn(outputDTO);

        PetTypeDTO result = petTypeService.createPetType(inputDTO);

        log.info("🎯 Esperado: ID no null, Nombre='Perro'");
        log.info("📊 Actual: ID={}, Nombre='{}'", result.getId(), result.getName());

        assertNotNull(result.getId(), "❌ ERROR: El ID no debería ser null");
        assertEquals("Perro", result.getName(),
                "❌ ERROR: Se esperaba 'Perro' pero fue '" + result.getName() + "'");

        verify(petTypeRepository, times(1)).save(any(PetType.class));
        log.info("✅ RESULTADO: Tipo creado correctamente con ID={}", result.getId());
    }

    /** ❌ CREATE - ERROR: tipo duplicado (DEBE FALLAR - X ROJA si el servicio no lanza excepción) */
    @Test
    void testCreatePetType_Error_Duplicate() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: CREAR TIPO DE MASCOTA (ERROR: NOMBRE DUPLICADO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        PetTypeDTO inputDTO = new PetTypeDTO();
        inputDTO.setName("Perro");

        log.info("🎯 Esperado: Lanzar RuntimeException por nombre duplicado");
        log.info("⚠️  Configuración: petTypeRepository.existsByName('Perro') → true");

        // ✅ CORRECTO: Mock devuelve true (el nombre ya existe)
        when(petTypeRepository.existsByName("Perro")).thenReturn(true);

        try {
            petTypeService.createPetType(inputDTO);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado RuntimeException por duplicado pero no se lanzó ninguna excepción");
        } catch (RuntimeException e) {
            log.info("✅ Actual: Se lanzó RuntimeException con mensaje: '{}'", e.getMessage());
            assertTrue(e.getMessage().contains("Ya existe"),
                    "❌ El mensaje debería contener 'Ya existe' pero fue: " + e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
        }
    }

    // ================================================================
    //  FIND - PRUEBAS DE BÚSQUEDA
    // ================================================================

    /** ✅ FIND por ID - ÉXITO (CHECK VERDE) */
    @Test
    void testFindPetTypeById() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BUSCAR TIPO POR ID (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Gato");

        PetTypeDTO outputDTO = new PetTypeDTO();
        outputDTO.setId(1);
        outputDTO.setName("Gato");

        when(petTypeRepository.findById(1)).thenReturn(Optional.of(petType));
        when(petTypeMapper.toDTO(petType)).thenReturn(outputDTO);

        PetTypeDTO result = petTypeService.getPetTypeById(1);

        log.info("🎯 Esperado: ID=1, Nombre='Gato'");
        log.info("📊 Actual: ID={}, Nombre='{}'", result.getId(), result.getName());

        assertEquals(1, result.getId(), "❌ ERROR: Se esperaba ID=1 pero fue ID=" + result.getId());
        assertEquals("Gato", result.getName(),
                "❌ ERROR: Se esperaba 'Gato' pero fue '" + result.getName() + "'");

        log.info("✅ RESULTADO: Tipo encontrado correctamente");
    }

    /** ❌ FIND por ID - ERROR: tipo no existe (DEBE FALLAR - X ROJA si el servicio no lanza excepción) */
    @Test
    void testFindPetTypeById_Error_NotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BUSCAR TIPO POR ID (ERROR: TIPO NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer nonExistentId = 999;
        log.info("🎯 Esperado: Lanzar PetTypeNotFoundException para ID={}", nonExistentId);
        log.info("⚠️  Configuración: petTypeRepository.findById({}) → Optional.empty()", nonExistentId);

        // ✅ CORRECTO: Mock devuelve Optional.empty()
        when(petTypeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        try {
            petTypeService.getPetTypeById(nonExistentId);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado PetTypeNotFoundException pero no se lanzó ninguna excepción");
        } catch (PetTypeNotFoundException e) {
            log.info("✅ Actual: Se lanzó PetTypeNotFoundException con mensaje: '{}'", e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
        }
    }

    /** ✅ FIND por nombre - ÉXITO (CHECK VERDE) */
    @Test
    void testFindPetTypeByName() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BUSCAR TIPO POR NOMBRE (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Ave");

        PetTypeDTO outputDTO = new PetTypeDTO();
        outputDTO.setId(1);
        outputDTO.setName("Ave");

        when(petTypeRepository.findByName("Ave")).thenReturn(Optional.of(petType));
        when(petTypeMapper.toDTO(petType)).thenReturn(outputDTO);

        PetTypeDTO result = petTypeService.getPetTypeByName("Ave");

        log.info("🎯 Esperado: Nombre='Ave'");
        log.info("📊 Actual: Nombre='{}'", result.getName());

        assertEquals("Ave", result.getName(),
                "❌ ERROR: Se esperaba 'Ave' pero fue '" + result.getName() + "'");

        log.info("✅ RESULTADO: Tipo encontrado correctamente");
    }

    /** ❌ FIND por nombre - ERROR: nombre no existe (DEBE FALLAR - X ROJA si el servicio no lanza excepción) */
    @Test
    void testFindPetTypeByName_Error_NotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: BUSCAR TIPO POR NOMBRE (ERROR: NOMBRE NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        String nonExistentName = "Inexistente123";
        log.info("🎯 Esperado: Lanzar PetTypeNotFoundException para nombre '{}'", nonExistentName);
        log.info("⚠️  Configuración: petTypeRepository.findByName('{}') → Optional.empty()", nonExistentName);

        // ✅ CORRECTO: Mock devuelve Optional.empty()
        when(petTypeRepository.findByName(nonExistentName)).thenReturn(Optional.empty());

        try {
            petTypeService.getPetTypeByName(nonExistentName);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado PetTypeNotFoundException pero no se lanzó ninguna excepción");
        } catch (PetTypeNotFoundException e) {
            log.info("✅ Actual: Se lanzó PetTypeNotFoundException con mensaje: '{}'", e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
        }
    }

    /** ✅ FIND todos - ÉXITO (CHECK VERDE) */
    @Test
    void testGetAllPetTypes() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: OBTENER TODOS LOS TIPOS (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        PetType type1 = new PetType(1, "Perro");
        PetType type2 = new PetType(2, "Gato");
        List<PetType> petTypes = Arrays.asList(type1, type2);

        PetTypeDTO dto1 = new PetTypeDTO(1, "Perro");
        PetTypeDTO dto2 = new PetTypeDTO(2, "Gato");

        when(petTypeRepository.findAll()).thenReturn(petTypes);
        when(petTypeMapper.toDTO(type1)).thenReturn(dto1);
        when(petTypeMapper.toDTO(type2)).thenReturn(dto2);

        List<PetTypeDTO> results = petTypeService.getAllPetTypes();

        log.info("🎯 Esperado: 2 tipos");
        log.info("📊 Actual: {} tipos", results.size());

        assertEquals(2, results.size(),
                "❌ ERROR: Se esperaban 2 tipos pero se encontraron " + results.size());

        log.info("✅ RESULTADO: Tipos encontrados: {}", results.stream().map(PetTypeDTO::getName).toList());
    }

    // ================================================================
    //  UPDATE - PRUEBAS DE ACTUALIZACIÓN
    // ================================================================

    /** ✅ UPDATE - ÉXITO (CHECK VERDE) */
    @Test
    void testUpdatePetType() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ACTUALIZAR TIPO (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        PetType existingPetType = new PetType();
        existingPetType.setId(1);
        existingPetType.setName("Perro");

        PetTypeDTO updateDTO = new PetTypeDTO();
        updateDTO.setName("Perro Actualizado");

        PetType updatedPetType = new PetType();
        updatedPetType.setId(1);
        updatedPetType.setName("Perro Actualizado");

        PetTypeDTO outputDTO = new PetTypeDTO();
        outputDTO.setId(1);
        outputDTO.setName("Perro Actualizado");

        when(petTypeRepository.findById(1)).thenReturn(Optional.of(existingPetType));
        when(petTypeRepository.save(any(PetType.class))).thenReturn(updatedPetType);
        when(petTypeMapper.toDTO(updatedPetType)).thenReturn(outputDTO);

        PetTypeDTO result = petTypeService.updatePetType(1, updateDTO);

        log.info("🎯 Esperado: Nombre='Perro Actualizado'");
        log.info("📊 Actual: Nombre='{}'", result.getName());

        assertEquals("Perro Actualizado", result.getName(),
                "❌ ERROR: Se esperaba 'Perro Actualizado' pero fue '" + result.getName() + "'");

        verify(petTypeRepository, times(1)).save(any(PetType.class));
        log.info("✅ RESULTADO: Tipo actualizado correctamente");
    }

    /** ❌ UPDATE - ERROR: tipo no existe (DEBE FALLAR - X ROJA si el servicio no lanza excepción) */
    @Test
    void testUpdatePetType_Error_NotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ACTUALIZAR TIPO (ERROR: TIPO NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer nonExistentId = 999;
        PetTypeDTO updateDTO = new PetTypeDTO();
        updateDTO.setName("Nuevo");

        log.info("🎯 Esperado: Lanzar PetTypeNotFoundException para ID={}", nonExistentId);
        log.info("⚠️  Configuración: petTypeRepository.findById({}) → Optional.empty()", nonExistentId);

        // ✅ CORRECTO: Mock devuelve Optional.empty()
        when(petTypeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        try {
            petTypeService.updatePetType(nonExistentId, updateDTO);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado PetTypeNotFoundException pero no se lanzó ninguna excepción");
        } catch (PetTypeNotFoundException e) {
            log.info("✅ Actual: Se lanzó PetTypeNotFoundException con mensaje: '{}'", e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
        }
    }

    // ================================================================
    //  DELETE - PRUEBAS DE ELIMINACIÓN
    // ================================================================

    /** ✅ DELETE por ID - ÉXITO (CHECK VERDE) */
    @Test
    void testDeletePetType() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ELIMINAR TIPO POR ID (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer petTypeId = 1;
        log.info("🎯 Esperado: Eliminar tipo con ID={} sin excepciones", petTypeId);
        log.info("⚠️  Configuración: petTypeRepository.existsById({}) → true", petTypeId);

        when(petTypeRepository.existsById(petTypeId)).thenReturn(true);
        doNothing().when(petTypeRepository).deleteById(petTypeId);

        assertDoesNotThrow(() -> petTypeService.deletePetType(petTypeId),
                "❌ ERROR: No debería lanzar excepción al eliminar");

        verify(petTypeRepository, times(1)).deleteById(petTypeId);
        log.info("✅ RESULTADO: Tipo eliminado correctamente");
    }

    /** ❌ DELETE por ID - ERROR: tipo no existe (DEBE FALLAR - X ROJA si el servicio no lanza excepción) */
    @Test
    void testDeletePetType_Error_NotFound() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: ELIMINAR TIPO POR ID (ERROR: TIPO NO EXISTE)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Integer nonExistentId = 999;
        log.info("🎯 Esperado: Lanzar PetTypeNotFoundException para ID={}", nonExistentId);
        log.info("⚠️  Configuración: petTypeRepository.existsById({}) → false", nonExistentId);

        // ✅ CORRECTO: Mock devuelve false
        when(petTypeRepository.existsById(nonExistentId)).thenReturn(false);

        try {
            petTypeService.deletePetType(nonExistentId);
            log.error("❌ ERROR: No se lanzó la excepción esperada");
            fail("❌ Debería haber lanzado PetTypeNotFoundException pero no se lanzó ninguna excepción");
        } catch (PetTypeNotFoundException e) {
            log.info("✅ Actual: Se lanzó PetTypeNotFoundException con mensaje: '{}'", e.getMessage());
            log.info("✅ RESULTADO: Prueba de error superada (la excepción se lanzó correctamente)");
        }
    }

    // ================================================================
    //  EXISTS - PRUEBAS DE VERIFICACIÓN
    // ================================================================

    /** ✅ EXISTS por ID - ÉXITO (CHECK VERDE) */
    @Test
    void testExistsById() {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📌 PRUEBA: VERIFICAR EXISTENCIA POR ID (CASO EXITOSO)");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        when(petTypeRepository.existsById(1)).thenReturn(true);
        when(petTypeRepository.existsById(999)).thenReturn(false);

        boolean exists = petTypeService.existsById(1);
        boolean notExists = petTypeService.existsById(999);

        log.info("🎯 Esperado: existsById(1)=true, existsById(999)=false");
        log.info("📊 Actual: existsById(1)={}, existsById(999)={}", exists, notExists);

        assertTrue(exists, "❌ ERROR: existsById(1) debería ser true");
        assertFalse(notExists, "❌ ERROR: existsById(999) debería ser false");

        log.info("✅ RESULTADO: Verificaciones correctas");
    }
}