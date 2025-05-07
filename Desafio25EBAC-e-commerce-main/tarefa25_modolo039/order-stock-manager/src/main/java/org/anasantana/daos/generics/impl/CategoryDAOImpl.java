package org.anasantana.daos.generics.impl;

import jakarta.persistence.EntityManager;

import org.anasantana.daos.generics.ICategoryDAO;
import org.anasantana.model.Category;

import java.util.List;

public class CategoryDAOImpl extends GenericDAOImpl<Category, Long> implements ICategoryDAO {

    public CategoryDAOImpl(EntityManager em) {
        super(Category.class, em);
    }

    @Override
    public List<Category> findByDescription(String description) {
        return em.createQuery(
                "SELECT c FROM Category c WHERE LOWER(c.description) LIKE LOWER(:desc)", Category.class)
                .setParameter("desc", "%" + description + "%")
                .getResultList();
    }
}
