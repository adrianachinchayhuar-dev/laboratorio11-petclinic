package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {

    public Owner mapToEntity(OwnerDTO dto) {
        if (dto == null) return null;
        return new Owner(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getAddress(),
                dto.getCity(),
                dto.getTelephone()
        );
    }

    public OwnerDTO mapToDto(Owner entity) {
        if (entity == null) return null;
        return new OwnerDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAddress(),
                entity.getCity(),
                entity.getTelephone()
        );
    }

}