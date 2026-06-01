package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper {

    @Autowired
    private OwnerRepository ownerRepository;

    public VisitDTO toDTO(Visit visit) {
        if (visit == null) {
            return null;
        }

        VisitDTO dto = new VisitDTO();
        dto.setId(visit.getId());
        dto.setDate(visit.getDate());
        dto.setDescription(visit.getDescription());

        if (visit.getPet() != null) {
            Pet pet = visit.getPet();
            dto.setPetId(pet.getId());
            dto.setPetName(pet.getName());

            // ✅ Usamos getOwnerId() que existe en Pet
            if (pet.getOwnerId() != 0) {
                ownerRepository.findById(pet.getOwnerId()).ifPresent(owner -> {
                    String ownerName = (owner.getFirstName() != null ? owner.getFirstName() : "") +
                            " " +
                            (owner.getLastName() != null ? owner.getLastName() : "");
                    dto.setOwnerName(ownerName.trim());
                });
            }
        }

        return dto;
    }

    public Visit toEntity(VisitDTO dto, Pet pet) {
        if (dto == null) {
            return null;
        }

        Visit visit = new Visit();
        visit.setId(dto.getId());
        visit.setDate(dto.getDate());
        visit.setDescription(dto.getDescription());
        visit.setPet(pet);

        return visit;
    }

    public void updateEntityFromDTO(VisitDTO dto, Visit visit, Pet pet) {
        if (dto == null) {
            return;
        }

        if (dto.getDate() != null) {
            visit.setDate(dto.getDate());
        }
        if (dto.getDescription() != null) {
            visit.setDescription(dto.getDescription());
        }
        if (pet != null) {
            visit.setPet(pet);
        }
    }
}