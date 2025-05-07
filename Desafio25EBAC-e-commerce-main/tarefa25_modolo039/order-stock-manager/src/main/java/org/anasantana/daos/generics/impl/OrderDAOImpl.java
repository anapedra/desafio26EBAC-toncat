package org.anasantana.daos.generics.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.anasantana.daos.generics.IOrderDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.model.Order;
import org.anasantana.model.User;

import java.time.LocalDate;
import java.util.List;

public class OrderDAOImpl extends GenericDAOImpl<Order, Long> implements IOrderDAO {

    public OrderDAOImpl(EntityManager em) {
        super(Order.class, em);
    }

    @Override
    public PageResult<Order> find(User client, String nameClient, String cpfClient, LocalDate min, LocalDate max, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT o FROM Order o JOIN o.client c WHERE 1=1");

        if (client != null) {
            jpql.append(" AND c = :client");
        }
        if (nameClient != null && !nameClient.isEmpty()) {
            jpql.append(" AND LOWER(c.name) LIKE LOWER(CONCAT('%', :nameClient, '%'))");
        }
        if (cpfClient != null && !cpfClient.isEmpty()) {
            jpql.append(" AND LOWER(c.cpf) LIKE LOWER(CONCAT('%', :cpfClient, '%'))");
        }
        jpql.append(" AND o.dateOrder BETWEEN :min AND :max");

        TypedQuery<Order> query = em.createQuery(jpql.toString(), Order.class);

        if (client != null) query.setParameter("client", client);
        if (nameClient != null && !nameClient.isEmpty()) query.setParameter("nameClient", nameClient);
        if (cpfClient != null && !cpfClient.isEmpty()) query.setParameter("cpfClient", cpfClient);
        query.setParameter("min", min);
        query.setParameter("max", max);

        int offset = page * size;
        List<Order> content = query.setFirstResult(offset).setMaxResults(size).getResultList();

        // Contagem
        String countJpql = jpql.toString().replace("SELECT DISTINCT o", "SELECT COUNT(DISTINCT o)");
        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
        if (client != null) countQuery.setParameter("client", client);
        if (nameClient != null && !nameClient.isEmpty()) countQuery.setParameter("nameClient", nameClient);
        if (cpfClient != null && !cpfClient.isEmpty()) countQuery.setParameter("cpfClient", cpfClient);
        countQuery.setParameter("min", min);
        countQuery.setParameter("max", max);

        long totalElements = countQuery.getSingleResult();

        return new PageResult<>(content, page, size, totalElements);
    }

    @Override
    public List<Order> findOrder(List<Order> orders) {
        return em.createQuery("SELECT o FROM Order o JOIN FETCH o.client WHERE o IN :orders", Order.class)
                 .setParameter("orders", orders)
                 .getResultList();
    }

    @Override
    public List<Order> findSummary(LocalDate min, LocalDate max) {
        return em.createQuery("SELECT o FROM Order o WHERE o.dateOrder BETWEEN :min AND :max", Order.class)
                 .setParameter("min", min)
                 .setParameter("max", max)
                 .getResultList();
    }

    @Override
    public List<Order> sumSale(LocalDate min, LocalDate max) {
        return em.createQuery("SELECT o FROM Order o WHERE o.dateOrder BETWEEN :min AND :max", Order.class)
                 .setParameter("min", min)
                 .setParameter("max", max)
                 .getResultList();
    }
}
