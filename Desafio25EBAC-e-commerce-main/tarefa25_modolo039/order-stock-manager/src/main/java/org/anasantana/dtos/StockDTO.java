package org.anasantana.dtos;

import org.anasantana.model.Stock;
import org.anasantana.model.enums.StockStatus;

import java.io.Serializable;
import java.util.Objects;

public class StockDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private StockStatus stockStatus;
    private int quantity;
    private Long productId;
    private String productName;

    public StockDTO() {
    }

    public StockDTO(Long id, StockStatus stockStatus, int quantity, Long productId, String productName) {
        this.id = id;
        this.stockStatus = stockStatus;
        this.quantity = quantity;
        this.productId = productId;
        this.productName = productName;
    }

    public StockDTO(Stock entity) {
        this.id = entity.getId();
        this.stockStatus = entity.getStockStatus();
        this.quantity = entity.getQuantity();
        this.productId = (entity.getProduct() != null) ? entity.getProduct().getId() : null;
        this.productName = (entity.getProduct() != null) ? entity.getProduct().getShortDescription() : null;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockDTO)) return false;
        StockDTO stockDTO = (StockDTO) o;
        return quantity == stockDTO.quantity &&
                Objects.equals(id, stockDTO.id) &&
                stockStatus == stockDTO.stockStatus &&
                Objects.equals(productId, stockDTO.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stockStatus, quantity, productId);
    }
}
