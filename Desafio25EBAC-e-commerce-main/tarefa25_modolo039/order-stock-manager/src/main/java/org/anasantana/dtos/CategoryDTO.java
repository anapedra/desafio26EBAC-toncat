package org.anasantana.dtos;

import jakarta.validation.constraints.NotBlank;
import org.anasantana.model.Category;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class CategoryDTO implements Serializable {
    private static final long serialVersionUID=1L;
    private Long id;
    @NotBlank(message = "Campo obrigatório")
    private String description;
    private Instant momentRegistration;
    private Instant momentUpdate;


    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String description, Instant momentRegistration, Instant momentUpdate) {
        this.id = id;
        this.description = description;
        this.momentRegistration = momentRegistration;
        this.momentUpdate = momentUpdate;
    }

    public CategoryDTO(Category entity) {
     id=entity.getId();
     description= entity.getDescription();
     momentRegistration=entity.getMomentRegistration();
     momentUpdate= entity.getMomentUpdate();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getMomentRegistration() {
        return momentRegistration;
    }

    public void setMomentRegistration(Instant momentRegistration) {
        this.momentRegistration = momentRegistration;
    }

    public Instant getMomentUpdate() {
        return momentUpdate;
    }

    public void setMomentUpdate(Instant momentUpdate) {
        this.momentUpdate = momentUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryDTO)) return false;
        CategoryDTO that = (CategoryDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}