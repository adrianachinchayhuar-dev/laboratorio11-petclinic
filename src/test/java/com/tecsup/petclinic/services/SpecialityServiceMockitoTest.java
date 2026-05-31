package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tecsup.petclinic.dtos.SpecialityDTO;
import com.tecsup.petclinic.entities.Speciality;
import com.tecsup.petclinic.exceptions.SpecialityNotFoundException;
import com.tecsup.petclinic.mappers.SpecialityMapper;
import com.tecsup.petclinic.repositories.SpecialityRepository;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
public class SpecialityServiceMockitoTest {

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private SpecialityMapper specialityMapper;

    @MockitoBean
    private SpecialityRepository repository;

    @Test
    public void testFindSpecialityById() {

        Speciality specialityExpected =
                new Speciality(
                        1,
                        "Radiology"
                );

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(specialityExpected));

        SpecialityDTO speciality = null;

        try {

            speciality =
                    this.specialityService.findById(1);

        } catch (SpecialityNotFoundException e) {

            fail(e.getMessage());
        }

        log.info("" + speciality);

        assertEquals(
                specialityExpected.getName(),
                speciality.getName()
        );
    }

    @Test
    public void testCreateSpeciality() {

        Speciality newSpeciality =
                new Speciality(
                        null,
                        "Surgery"
                );

        Speciality specialityCreated =
                new Speciality(
                        1,
                        "Surgery"
                );

        SpecialityDTO newSpecialityDTO =
                specialityMapper.mapToDto(newSpeciality);

        Mockito.when(this.repository.save(newSpeciality))
                .thenReturn(specialityCreated);

        SpecialityDTO specialityDTOCreated =
                this.specialityService.create(newSpecialityDTO);

        log.info(
                "Speciality created: {}",
                specialityDTOCreated
        );

        assertNotNull(specialityDTOCreated.getId());

        assertEquals(
                "Surgery",
                specialityDTOCreated.getName()
        );
    }

    @Test
    public void testUpdateSpeciality() {

        Speciality speciality =
                new Speciality(
                        1,
                        "Surgery"
                );

        SpecialityDTO specialityDTO =
                specialityMapper.mapToDto(speciality);

        specialityDTO.setName("Dentistry");

        Speciality specialityUpdate =
                specialityMapper.mapToEntity(specialityDTO);

        Mockito.when(this.repository.save(specialityUpdate))
                .thenReturn(specialityUpdate);

        SpecialityDTO specialityDTOUpdate =
                this.specialityService.update(specialityDTO);

        log.info(
                "Speciality updated: {}",
                specialityDTOUpdate
        );

        assertEquals(
                "Dentistry",
                specialityDTOUpdate.getName()
        );
    }

    @Test
    public void testDeleteSpeciality() {

        Speciality speciality =
                new Speciality(
                        1,
                        "Surgery"
                );

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.of(speciality));

        Mockito.doNothing()
                .when(this.repository)
                .delete(speciality);

        try {

            this.specialityService.delete(1);

        } catch (SpecialityNotFoundException e) {

            fail(e.getMessage());
        }

        Mockito.when(this.repository.findById(1))
                .thenReturn(Optional.empty());

        try {

            this.specialityService.findById(1);

            assertTrue(false);

        } catch (SpecialityNotFoundException e) {

            assertTrue(true);
        }
    }

    @Test
    public void testFindSpecialityByName() {

        String NAME = "Surgery";

        List<Speciality> specialitiesExpected =
                new ArrayList<>();

        specialitiesExpected.add(
                new Speciality(
                        1,
                        "Surgery"
                )
        );

        Mockito.when(this.repository.findByName(NAME))
                .thenReturn(specialitiesExpected);

        List<SpecialityDTO> specialities =
                this.specialityService.findByName(NAME);

        assertEquals(
                specialitiesExpected.size(),
                specialities.size()
        );
    }
}