package com.tecsup.petclinic.dtos;

import java.time.LocalDate;

public class VisitDTO {

    private Integer id;  // ← Integer
    private LocalDate date;
    private String description;
    private Integer petId;  // ← Integer
    private String petName;
    private String ownerName;

    public VisitDTO() {}

    public VisitDTO(Integer id, LocalDate date, String description, Integer petId) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.petId = petId;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}