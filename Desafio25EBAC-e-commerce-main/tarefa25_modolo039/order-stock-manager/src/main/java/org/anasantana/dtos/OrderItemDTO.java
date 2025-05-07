package org.anasantana.dtos;


import org.anasantana.model.OrderItem;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemDTO implements Serializable {
    private static final long serialVersionUID=1L;

    private Long productId;
    private String productDescription;
    private Integer quantity;
    private Double productPrice;
    private Double subTotal;
    private String productImgUrl;

    public OrderItemDTO() {

    }

    public OrderItemDTO(Long productId,Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;


    }

    public OrderItemDTO(OrderItem entity) {
       productId = entity.getProduct().getId();
       productDescription = entity.getProduct().getShortDescription();
       quantity = entity.getQuantity();
       subTotal = entity.getSubTotal();
       productPrice = entity.getProduct().getInitialPrice();
       productImgUrl = entity.getProduct().getImgUrl();

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDTO that = (OrderItemDTO) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }
}

