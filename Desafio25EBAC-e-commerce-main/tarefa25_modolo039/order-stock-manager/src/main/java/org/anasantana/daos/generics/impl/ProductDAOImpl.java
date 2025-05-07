package org.anasantana.daos.generics.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.anasantana.daos.generics.IProductDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.model.Category;
import org.anasantana.model.Product;

import java.util.List;

public class ProductDAOImpl extends GenericDAOImpl<Product, Long> implements IProductDAO {

    public ProductDAOImpl(EntityManager em) {
        super(Product.class, em);
    }

    @Override
    public PageResult<Product> find(List<Category> categories, String categoryDescription, String productDescription, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT p FROM Product p JOIN p.categories c WHERE 1=1");

        if (categories != null && !categories.isEmpty()) {
            jpql.append(" AND c IN :categories");
        }
        if (categoryDescription != null && !categoryDescription.trim().isEmpty()) {
            jpql.append(" AND LOWER(c.description) LIKE LOWER(CONCAT('%', :categoryDescription, '%'))");
        }
        if (productDescription != null && !productDescription.trim().isEmpty()) {
            jpql.append(" AND LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :productDescription, '%'))");
        }

        TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);
        if (categories != null && !categories.isEmpty()) {
            query.setParameter("categories", categories);
        }
        if (categoryDescription != null && !categoryDescription.trim().isEmpty()) {
            query.setParameter("categoryDescription", categoryDescription);
        }
        if (productDescription != null && !productDescription.trim().isEmpty()) {
            query.setParameter("productDescription", productDescription);
        }

        int offset = page * size;
        List<Product> content = query.setFirstResult(offset).setMaxResults(size).getResultList();

        String countJpql = jpql.toString().replaceFirst("SELECT DISTINCT p", "SELECT COUNT(DISTINCT p)");
        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
        if (categories != null && !categories.isEmpty()) {
            countQuery.setParameter("categories", categories);
        }
        if (categoryDescription != null && !categoryDescription.trim().isEmpty()) {
            countQuery.setParameter("categoryDescription", categoryDescription);
        }
        if (productDescription != null && !productDescription.trim().isEmpty()) {
            countQuery.setParameter("productDescription", productDescription);
        }

        Long total = countQuery.getSingleResult();

        return new PageResult<>(content, page, size, total);
    }

    @Override
    public List<Product> findProducts(List<Product> products) {
        return em.createQuery("SELECT p FROM Product p JOIN FETCH p.categories WHERE p IN :products", Product.class)
                 .setParameter("products", products)
                 .getResultList();
    }
}
