package org.anasantana.services;

import org.anasantana.daos.generics.IGenericDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.model.Order;
import org.anasantana.model.User;

import java.time.LocalDate;
import java.util.List;

public interface IOrderDAO extends IGenericDAO<Order, Long> {

    PageResult<Order> find(User client, String nameClient, String cpfClient, LocalDate min, LocalDate max, int page, int size);

    List<Order> findOrder(List<Order> orders);

    List<Order> findSummary(LocalDate min, LocalDate max);

    List<Order> sumSale(LocalDate min, LocalDate max);
}
