package org.anasantana.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.anasantana.model.enums.OrderStatus;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_order")
public class Order implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    @Column(name = "date_order")
    private LocalDate dateOrder;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    @Column(name = "moment")
    private Instant moment;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();

    public Order(Long id,Instant moment,LocalDate dateOrder, OrderStatus status, User client) {
        this.id = id;
        this.dateOrder = dateOrder;
        this.moment = moment;
        setStatus(status);
        this.client = client;
    }

    public Order() {

    }
    public int getQuantityProduct(){
        int soma = 0;
        for (OrderItem orderItem : items) {
            soma += orderItem.getQuantity();
        }
        return soma;
    }

    public double getTotal(){
        double soma = 0.0;
        for (OrderItem orderItem : items) {
            soma += orderItem.getSubTotal();
        }
        return soma;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(LocalDate dateOrder) {
        this.dateOrder = dateOrder;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }


    public void setClient(User client) {
        this.client = client;
    }

    public User getClient() {
        return client;
    }

    public OrderStatus getStatus() {
        return OrderStatus.valueOf(status);
    }

    public void setStatus(OrderStatus status) {
        if (status != null){
            this.status = status.getCode();
        }
    }

    public Set<OrderItem> getItems() {
        return items;

    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public List<Product> getProducts(){
        return items.stream().map(x->x.getProduct()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}















