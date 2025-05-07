package org.anasantana.daos.generics.impl;

import jakarta.persistence.EntityManager;
import org.anasantana.model.ProductRestocking;
import org.anasantana.daos.generics.IProductRestockingDAO;

public class ProductRestockingDAOImpl extends GenericDAOImpl<ProductRestocking, Long> implements IProductRestockingDAO {

    public ProductRestockingDAOImpl(EntityManager em) {
        super(ProductRestocking.class, em);
    }

}
