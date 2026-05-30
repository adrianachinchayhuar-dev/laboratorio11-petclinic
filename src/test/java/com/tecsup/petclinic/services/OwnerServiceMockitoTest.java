package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mappers.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
public class OwnerServiceMockitoTest {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerMapper ownerMapper;

    @MockitoBean
    private OwnerRepository repository;

    /**
     * Buscar owner por ID
     */
    @Test
    public void testFindOwnerById() {

        Owner ownerExpected = new Owner(
                1,
                "Juan",
                "Perez",
                "Av Lima 123",
                "Lima",
                "999888777"
        );

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(ownerExpected));

        OwnerDTO owner = null;

        try {
            owner = this.ownerService.findById(1);
        } catch (OwnerNotFoundException e) {
            fail(e.getMessage());
        }

        log.info("" + owner);

        assertEquals(ownerExpected.getFirstName(), owner.getFirstName());
        assertEquals(ownerExpected.getLastName(), owner.getLastName());
    }

    /**
     * Crear owner
     */
    @Test
    public void testCreateOwner() {

        Owner newOwner = new Owner(
                null,
                "Maria",
                "Lopez",
                "Av Peru 456",
                "Cusco",
                "987654321"
        );

        Owner ownerCreated = new Owner(
                1,
                "Maria",
                "Lopez",
                "Av Peru 456",
                "Cusco",
                "987654321"
        );

        OwnerDTO newOwnerDTO = ownerMapper.mapToDto(newOwner);

        Mockito.when(this.repository.save(newOwner))
                .thenReturn(ownerCreated);

        OwnerDTO ownerDTOCreated = this.ownerService.create(newOwnerDTO);

        log.info("Owner created: {}", ownerDTOCreated);

        assertNotNull(ownerDTOCreated.getId());

        assertEquals("Maria", ownerDTOCreated.getFirstName());
        assertEquals("Lopez", ownerDTOCreated.getLastName());
        assertEquals("Cusco", ownerDTOCreated.getCity());
    }

    /**
     * Actualizar owner
     */
    @Test
    public void testUpdateOwner() {

        Owner owner = new Owner(
                1,
                "Maria",
                "Lopez",
                "Av Peru 456",
                "Cusco",
                "987654321"
        );

        OwnerDTO ownerDTO = ownerMapper.mapToDto(owner);

        // NUEVOS DATOS
        ownerDTO.setFirstName("Ana");
        ownerDTO.setLastName("Torres");
        ownerDTO.setCity("Arequipa");

        Owner ownerUpdate = ownerMapper.mapToEntity(ownerDTO);

        Mockito.when(this.repository.save(ownerUpdate))
                .thenReturn(ownerUpdate);

        OwnerDTO ownerDTOUpdate = this.ownerService.update(ownerDTO);

        log.info("Owner updated: {}", ownerDTOUpdate);

        assertEquals("Ana", ownerDTOUpdate.getFirstName());
        assertEquals("Torres", ownerDTOUpdate.getLastName());
        assertEquals("Arequipa", ownerDTOUpdate.getCity());
    }

    /**
     * Eliminar owner
     */
    @Test
    public void testDeleteOwner() {

        Owner owner = new Owner(
                1,
                "Maria",
                "Lopez",
                "Av Peru 456",
                "Cusco",
                "987654321"
        );

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(owner));

        Mockito.doNothing()
                .when(this.repository)
                .delete(owner);

        try {
            this.ownerService.delete(1);
        } catch (OwnerNotFoundException e) {
            fail(e.getMessage());
        }

        // VALIDAR ELIMINACIÓN

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.empty());

        try {

            this.ownerService.findById(1);

            assertTrue(false);

        } catch (OwnerNotFoundException e) {

            assertTrue(true);
        }
    }

    /**
     * Buscar owner por firstName
     */
    @Test
    public void testFindOwnerByFirstName() {

        String FIRST_NAME = "Maria";

        List<Owner> ownersExpected = new ArrayList<>();

        ownersExpected.add(
                new Owner(
                        1,
                        "Maria",
                        "Lopez",
                        "Av Peru 456",
                        "Cusco",
                        "987654321"
                )
        );

        Mockito.when(this.repository.findByFirstName(FIRST_NAME))
                .thenReturn(ownersExpected);

        List<OwnerDTO> owners =
                this.ownerService.findByFirstName(FIRST_NAME);

        assertEquals(ownersExpected.size(), owners.size());
    }
}