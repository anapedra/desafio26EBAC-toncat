package org.anasantana.daos.generics.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.anasantana.daos.generics.IGenericDAO;
import org.anasantana.daos.pagination.PageResult;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class GenericDAOImpl<T, ID extends Serializable> implements IGenericDAO<T, ID> {

    private final Class<T> entityClass;

    @PersistenceContext
    protected EntityManager em;

    protected GenericDAOImpl(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    @Override
    public T save(T entity) {
        return em.merge(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        T entity = em.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findAll() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, entityClass).getResultList();
    }

    @Override
    public T getOne(ID id) {
        return em.getReference(entityClass, id);
    }

    @Override
    public void deleteById(ID id) {
        T entity = getOne(id);
        em.remove(entity);
    }

    @Override
    public PageResult<T> findAllPaged(int page, int size) {
        int offset = page * size;

        List<T> content = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();

        Long totalElements = em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                .getSingleResult();

        return new PageResult<>(content, page, size, totalElements);
    }
}
