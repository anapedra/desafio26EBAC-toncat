package org.anasantana.daos.generics;

import org.anasantana.model.OrderItem;

import java.util.List;

public interface IOrderItemDAO extends IGenericDAO<OrderItem, Long> {
    void saveAll(List<OrderItem> items);
}
