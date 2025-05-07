package org.anasantana.daos.generics.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.anasantana.daos.generics.IStockDAO;
import org.anasantana.model.Stock;
import org.anasantana.services.exceptions.DAOException;

public class StockDAOImpl extends GenericDAOImpl<Stock, Long> implements IStockDAO {

    public StockDAOImpl(EntityManager em) {
        super(Stock.class, em);
    }

    @Override
    public Stock findByProductId(Long productId) throws DAOException {
        try {
            TypedQuery<Stock> query = em.createQuery(
                "SELECT s FROM Stock s WHERE s.product.id = :productId", Stock.class);
            query.setParameter("productId", productId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // ou Optional.empty() se preferir trabalhar com Optional
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar estoque por produto ID: " + productId, e);
        }
    }
}
