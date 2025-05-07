package org.anasantana.model;

import jakarta.persistence.*;

import org.anasantana.model.enums.StockStatus;

import java.io.Serializable;
import java.util.Objects;

@Table(name = "tb_stock")
@Entity
public class Stock implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stock_status")
    private Integer stockStatus;

    @Column(name = "quantity")
    private int quantity;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Stock() {
    }

    public Stock(Long id, StockStatus stockStatus) {
        this.id = id;
        setStockStatus(stockStatus);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
       this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StockStatus getStockStatus(){
        return StockStatus.valueOf(stockStatus);
    }

    public void setStockStatus(StockStatus stockStatus){
        if (stockStatus != null) {
            this.stockStatus = stockStatus.getCode();
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
