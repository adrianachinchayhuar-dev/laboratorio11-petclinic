package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.repositories.VetRepository;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
public class VetServiceMockitoTest {

    @Autowired
    private VetService vetService;

    @Autowired
    private VetMapper vetMapper;

    @MockitoBean
    private VetRepository repository;

    @Test
    public void testFindVetById() {

        Vet vetExpected = new Vet(
                1,
                "Carlos",
                "Ramirez"
        );

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(vetExpected));

        VetDTO vet = null;

        try {
            vet = this.vetService.findById(1);
        } catch (VetNotFoundException e) {
            fail(e.getMessage());
        }

        log.info("" + vet);

        assertEquals(vetExpected.getFirstName(), vet.getFirstName());
        assertEquals(vetExpected.getLastName(), vet.getLastName());
    }

    @Test
    public void testCreateVet() {

        Vet newVet = new Vet(
                null,
                "Luis",
                "Gomez"
        );

        Vet vetCreated = new Vet(
                1,
                "Luis",
                "Gomez"
        );

        VetDTO newVetDTO = vetMapper.mapToDto(newVet);

        Mockito.when(this.repository.save(newVet))
                .thenReturn(vetCreated);

        VetDTO vetDTOCreated = this.vetService.create(newVetDTO);

        log.info("Vet created: {}", vetDTOCreated);

        assertNotNull(vetDTOCreated.getId());
        assertEquals("Luis", vetDTOCreated.getFirstName());
        assertEquals("Gomez", vetDTOCreated.getLastName());
    }

    @Test
    public void testUpdateVet() {

        Vet vet = new Vet(
                1,
                "Luis",
                "Gomez"
        );

        VetDTO vetDTO = vetMapper.mapToDto(vet);

        vetDTO.setFirstName("Pedro");
        vetDTO.setLastName("Salazar");

        Vet vetUpdate = vetMapper.mapToEntity(vetDTO);

        Mockito.when(this.repository.save(vetUpdate))
                .thenReturn(vetUpdate);

        VetDTO vetDTOUpdate = this.vetService.update(vetDTO);

        log.info("Vet updated: {}", vetDTOUpdate);

        assertEquals("Pedro", vetDTOUpdate.getFirstName());
        assertEquals("Salazar", vetDTOUpdate.getLastName());
    }

    @Test
    public void testDeleteVet() {

        Vet vet = new Vet(
                1,
                "Luis",
                "Gomez"
        );

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(vet));

        Mockito.doNothing()
                .when(this.repository)
                .delete(vet);

        try {
            this.vetService.delete(1);
        } catch (VetNotFoundException e) {
            fail(e.getMessage());
        }

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.empty());

        try {
            this.vetService.findById(1);
            assertTrue(false);
        } catch (VetNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testFindVetByFirstName() {

        String FIRST_NAME = "Luis";

        List<Vet> vetsExpected = new ArrayList<>();

        vetsExpected.add(
                new Vet(
                        1,
                        "Luis",
                        "Gomez"
                )
        );

        Mockito.when(this.repository.findByFirstName(FIRST_NAME))
                .thenReturn(vetsExpected);

        List<VetDTO> vets = this.vetService.findByFirstName(FIRST_NAME);

        assertEquals(vetsExpected.size(), vets.size());
    }
}