package org.anasantana.dtos;

import jakarta.validation.constraints.NotEmpty;
import org.anasantana.model.Order;
import org.anasantana.model.OrderItem;
import org.anasantana.model.enums.OrderStatus;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OrderDTO implements Serializable {
    private static final long serialVersionUID=1L;
    private Instant moment;
    private Long id;
    private OrderStatus status;
    private Integer totalProduct;
    private LocalDate dateOrder;
    private Double total;

    private ClientDTO client;
    @NotEmpty(message = "Deverá conter ao menos um item no pedido")
    private List<OrderItemDTO>items=new ArrayList<>();

    public OrderDTO(){

    }

    public OrderDTO(Long id, Instant moment, OrderStatus status, ClientDTO client,LocalDate dateOrder) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.dateOrder=dateOrder;

    }

    public OrderDTO(Order entity) {
        id = entity.getId();
        moment = entity.getMoment();
        client = new ClientDTO(entity.getClient());
        totalProduct = entity.getQuantityProduct();
        dateOrder=entity.getDateOrder();
        total = entity.getTotal();
    }


    public OrderDTO(Order entity, Set<OrderItem> orderItems){
        this(entity);
        entity.getItems().forEach(orderItem -> this.items.add(new OrderItemDTO(orderItem)));

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public Integer getTotalProduct() {
        return totalProduct;
    }

    public Double getTotal() {
        return total;
    }

    public LocalDate getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(LocalDate dateOrder) {
        this.dateOrder = dateOrder;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDTO)) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}