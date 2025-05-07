package org.anasantana.services.impl;

import org.anasantana.daos.generics.IStockDAO;
import org.anasantana.model.Product;
import org.anasantana.model.Stock;
import org.anasantana.services.exceptions.EntityNotFoundException;

public class StockService {

    private final IStockDAO stockDAO;

    public StockService(IStockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    public void decreaseStock(Product product, int quantity) {
        try {
            Stock stock = stockDAO.findByProductId(product.getId());
            if (stock == null) {
                throw new EntityNotFoundException("Stock for product id " + product.getId() + " not found");
            }
            if (stock.getQuantity() < quantity) {
                throw new IllegalArgumentException("Not enough stock available for product: " + product.getId());
            }
            stock.setQuantity(stock.getQuantity() - quantity);
            stockDAO.save(stock);
        } catch (Exception e) {
            throw new EntityNotFoundException("Failed to decrease stock for product: " + product.getId(), e);
        }
    }

    public void increaseStock(Product product, int quantity) {
        try {
            Stock stock = stockDAO.findByProductId(product.getId());
            if (stock == null) {
                throw new EntityNotFoundException("Stock for product id " + product.getId() + " not found");
            }
            stock.setQuantity(stock.getQuantity() + quantity);
            stockDAO.save(stock);
        } catch (Exception e) {
            throw new EntityNotFoundException("Failed to increase stock for product: " + product.getId(), e);
        }
    }
}
