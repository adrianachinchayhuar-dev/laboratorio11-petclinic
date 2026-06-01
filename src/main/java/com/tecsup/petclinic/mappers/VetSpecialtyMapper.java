package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.Speciality;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.entities.VetSpecialty;
import org.springframework.stereotype.Component;

@Component
public class VetSpecialtyMapper {

    public VetSpecialtyDTO toDTO(VetSpecialty vetSpecialty) {
        if (vetSpecialty == null) {
            return null;
        }

        VetSpecialtyDTO dto = new VetSpecialtyDTO();
        dto.setId(vetSpecialty.getId());

        if (vetSpecialty.getVet() != null) {
            dto.setVetId(vetSpecialty.getVet().getId());
            dto.setVetFirstName(vetSpecialty.getVet().getFirstName());
            dto.setVetLastName(vetSpecialty.getVet().getLastName());
        }

        // ✅ CORREGIDO: getSpecialty (no getSpeciality)
        if (vetSpecialty.getSpecialty() != null) {
            dto.setSpecialtyId(vetSpecialty.getSpecialty().getId());
            dto.setSpecialtyName(vetSpecialty.getSpecialty().getName());
        }

        return dto;
    }

    public VetSpecialty toEntity(VetSpecialtyDTO dto, Vet vet, Speciality specialty) {
        if (dto == null) {
            return null;
        }

        VetSpecialty vetSpecialty = new VetSpecialty();
        vetSpecialty.setId(dto.getId());
        vetSpecialty.setVet(vet);
        // ✅ CORREGIDO: setSpecialty (no setSpeciality)
        vetSpecialty.setSpecialty(specialty);

        return vetSpecialty;
    }
}