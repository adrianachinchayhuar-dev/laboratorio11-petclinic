package com.tecsup.petclinic.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.PetNotFoundException;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;

@SpringBootTest
@Transactional
public class VisitServiceTest {

    @Autowired
    private VisitService visitService;

    /**
     * Buscar visita por ID existente
     */
    @Test
    public void testFindVisitById() {

        Integer ID = 1;
        VisitDTO visit = visitService.getVisitById(ID);

        assertThat(visit, notNullValue());
        assertThat(visit.getId(), is(ID));
    }

    /**
     * Buscar visita por ID no existente - debe lanzar excepción
     */
    @Test
    public void testFindVisitByIdNotFound() {

        Integer ID = 999;

        assertThrows(VisitNotFoundException.class, () -> {
            visitService.getVisitById(ID);
        });
    }

    /**
     * Buscar visitas por petId
     */
    @Test
    public void testFindVisitsByPetId() {

        Integer PET_ID = 1;
        int SIZE_EXPECTED = 2;

        List<VisitDTO> visits = visitService.getVisitsByPetId(PET_ID);

        assertThat(visits.size(), is(SIZE_EXPECTED));
    }

    /**
     * Buscar visitas por petId no existente - debe retornar lista vacía
     */
    @Test
    public void testFindVisitsByPetIdNotFound() {

        Integer PET_ID = 999;

        List<VisitDTO> visits = visitService.getVisitsByPetId(PET_ID);

        assertThat(visits.size(), is(0));
    }

    /**
     * Buscar todas las visitas
     */
    @Test
    public void testFindAllVisits() {

        List<VisitDTO> visits = visitService.getAllVisits();

        assertThat(visits.size(), is(notNullValue()));
    }

    /**
     * Crear visita
     */
    @Test
    public void testCreateVisit() {

        Integer PET_ID = 1;
        String DESCRIPTION = "Vacunación anual";

        VisitDTO visit = new VisitDTO(null, LocalDate.now(), DESCRIPTION, PET_ID);

        VisitDTO visitCreated = visitService.createVisit(visit);

        assertThat(visitCreated.getId(), notNullValue());
        assertThat(visitCreated.getDescription(), is(DESCRIPTION));
        assertThat(visitCreated.getPetId(), is(PET_ID));
    }

    /**
     * Crear visita con fecha automática
     */
    @Test
    public void testCreateVisitWithAutoDate() {

        Integer PET_ID = 1;
        String DESCRIPTION = "Visita sin fecha";

        VisitDTO visit = new VisitDTO(null, null, DESCRIPTION, PET_ID);

        VisitDTO visitCreated = visitService.createVisit(visit);

        assertThat(visitCreated.getId(), notNullValue());
        assertThat(visitCreated.getDate(), notNullValue());
        assertThat(visitCreated.getDescription(), is(DESCRIPTION));
    }

    /**
     * Crear visita con mascota no existente - debe lanzar excepción
     */
    @Test
    public void testCreateVisitWithPetNotFound() {

        Integer PET_ID = 999;
        String DESCRIPTION = "Visita inválida";

        VisitDTO visit = new VisitDTO(null, LocalDate.now(), DESCRIPTION, PET_ID);

        assertThrows(PetNotFoundException.class, () -> {
            visitService.createVisit(visit);
        });
    }

    /**
     * Actualizar visita
     */
    @Test
    public void testUpdateVisit() {

        Integer PET_ID = 1;
        String FIRST_DESCRIPTION = "Descripción original";
        String UP_DESCRIPTION = "Descripción actualizada";

        // Crear una visita
        VisitDTO visit = new VisitDTO(null, LocalDate.now(), FIRST_DESCRIPTION, PET_ID);
        VisitDTO visitCreated = visitService.createVisit(visit);

        Integer createId = visitCreated.getId();

        // Actualizar
        visitCreated.setDescription(UP_DESCRIPTION);

        VisitDTO visitUpdated = visitService.updateVisit(createId, visitCreated);

        assertThat(visitUpdated.getId(), is(createId));
        assertThat(visitUpdated.getDescription(), is(UP_DESCRIPTION));
    }

    /**
     * Actualizar visita no existente - debe lanzar excepción
     */
    @Test
    public void testUpdateVisitNotFound() {

        Integer ID = 999;
        VisitDTO updateDTO = new VisitDTO();
        updateDTO.setDescription("Nueva descripción");

        assertThrows(VisitNotFoundException.class, () -> {
            visitService.updateVisit(ID, updateDTO);
        });
    }

    /**
     * Eliminar visita
     */
    @Test
    public void testDeleteVisit() {

        Integer PET_ID = 1;
        String DESCRIPTION = "Visita a eliminar";

        // Crear una visita
        VisitDTO visit = new VisitDTO(null, LocalDate.now(), DESCRIPTION, PET_ID);
        VisitDTO visitCreated = visitService.createVisit(visit);

        // Eliminar
        visitService.deleteVisit(visitCreated.getId());

        // Verificar que ya no existe
        assertThrows(VisitNotFoundException.class, () -> {
            visitService.getVisitById(visitCreated.getId());
        });
    }

    /**
     * Eliminar visita no existente - debe lanzar excepción
     */
    @Test
    public void testDeleteVisitNotFound() {

        Integer ID = 999;

        assertThrows(VisitNotFoundException.class, () -> {
            visitService.deleteVisit(ID);
        });
    }

    /**
     * Eliminar todas las visitas de una mascota
     */
    @Test
    public void testDeleteVisitsByPetId() {

        Integer PET_ID = 1;

        // Verificar que hay visitas
        List<VisitDTO> visitsBefore = visitService.getVisitsByPetId(PET_ID);
        assertThat(visitsBefore.size(), is(notNullValue()));

        // Eliminar todas las visitas de la mascota
        visitService.deleteVisitsByPetId(PET_ID);

        // Verificar que ya no hay visitas
        List<VisitDTO> visitsAfter = visitService.getVisitsByPetId(PET_ID);
        assertThat(visitsAfter.size(), is(0));
    }
}