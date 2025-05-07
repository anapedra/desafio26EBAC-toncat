package org.anasantana.daos.generics.impl;

import jakarta.persistence.EntityManager;
import org.anasantana.daos.generics.IOrderItemDAO;
import org.anasantana.model.OrderItem;

import java.util.List;

public class OrderItemDAOImpl extends GenericDAOImpl<OrderItem, Long> implements IOrderItemDAO {

    public OrderItemDAOImpl(EntityManager em) {
        super(OrderItem.class, em);
    }

    @Override
    public void saveAll(List<OrderItem> items) {
        for (OrderItem item : items) {
            em.persist(item);
        }
    }
}
