package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import org.springframework.stereotype.Component;

@Component
public class PetTypeMapper {

    public PetTypeDTO toDTO(PetType petType) {
        if (petType == null) {
            return null;
        }

        PetTypeDTO dto = new PetTypeDTO();
        dto.setId(petType.getId());
        dto.setName(petType.getName());

        return dto;
    }

    public PetType toEntity(PetTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        PetType petType = new PetType();
        petType.setId(dto.getId());
        petType.setName(dto.getName());

        return petType;
    }

    public void updateEntityFromDTO(PetTypeDTO dto, PetType petType) {
        if (dto.getName() != null) {
            petType.setName(dto.getName());
        }
    }
}