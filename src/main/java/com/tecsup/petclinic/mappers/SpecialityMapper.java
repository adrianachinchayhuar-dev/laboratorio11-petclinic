package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.SpecialityDTO;
import com.tecsup.petclinic.entities.Speciality;
import org.springframework.stereotype.Component;

@Component
public class SpecialityMapper {

    public Speciality mapToEntity(SpecialityDTO dto) {

        if (dto == null) return null;

        return new Speciality(
                dto.getId(),
                dto.getName()
        );
    }

    public SpecialityDTO mapToDto(Speciality entity) {

        if (entity == null) return null;

        return new SpecialityDTO(
                entity.getId(),
                entity.getName()
        );
    }
}