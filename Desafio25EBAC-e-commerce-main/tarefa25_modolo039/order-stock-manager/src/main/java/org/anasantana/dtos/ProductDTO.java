package org.anasantana.dtos;


import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.anasantana.model.Category;
import org.anasantana.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProductDTO implements Serializable {
    private static final long serialVersionUID=1L;
    private Long id;
    @NotBlank(message = "Campo obrigatório")
    private String shortDescription;
    @Lob
    @NotBlank(message = "Campo obrigatório")
    private String fullDescription;
    private Double initialPrice;
    private String imgUrl;
    @NotEmpty(message = "Deverá conter ao menos uma categoria")
    private List<CategoryDTO> categories=new ArrayList<>();


    public ProductDTO() {

    }
    public ProductDTO(Long id, String shortDescription, String fullDescription,
                      Double initialPrice, String imgUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.initialPrice = initialPrice;
        this.imgUrl = imgUrl;

    }

    public ProductDTO(Product entity){
        id=entity.getId();
        shortDescription=entity.getShortDescription();
        fullDescription=entity.getFullDescription();
        initialPrice= entity.getInitialPrice();
        imgUrl=entity.getImgUrl();
    }

     public ProductDTO(Product entity, Set<Category> categories){
        this(entity);
        entity.getCategories().forEach(category -> this.categories.add(new CategoryDTO(category)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Double getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(Double initialPrice) {
        this.initialPrice = initialPrice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }




    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDTO)) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}