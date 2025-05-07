package org.anasantana.dtos;

import org.anasantana.model.ProductRestocking;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class ProductRestockingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer quantity;
    private Instant moment;
    private Long stockId;

    public ProductRestockingDTO() {
    }

    public ProductRestockingDTO(Long id, Integer quantity, Instant moment, Long stockId) {
        this.id = id;
        this.quantity = quantity;
        this.moment = moment;
        this.stockId = stockId;
    }

    public ProductRestockingDTO(ProductRestocking entity) {
        this.id = entity.getId();
        this.quantity = entity.getQuantity();
        this.moment = entity.getMoment();
        this.stockId = (entity.getStock() != null) ? entity.getStock().getId() : null; // Operador ternário certinho
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductRestockingDTO)) return false;
        ProductRestockingDTO that = (ProductRestockingDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
