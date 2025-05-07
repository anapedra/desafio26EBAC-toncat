package org.anasantana.daos.generics;

import org.anasantana.model.Stock;
import org.anasantana.services.exceptions.DAOException;

public interface IStockDAO extends  IGenericDAO<Stock, Long>{
    Stock findByProductId(Long productId) throws DAOException;
}
