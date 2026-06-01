package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VetSpecialtyDTO {

    private Integer id;
    private Integer vetId;
    private String vetFirstName;
    private String vetLastName;
    private Integer specialtyId;
    private String specialtyName;
}