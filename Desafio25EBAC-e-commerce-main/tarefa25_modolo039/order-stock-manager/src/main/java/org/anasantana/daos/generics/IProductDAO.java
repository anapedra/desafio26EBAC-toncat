package org.anasantana.daos.generics;

import org.anasantana.daos.pagination.PageResult;
import org.anasantana.model.Category;
import org.anasantana.model.Product;

import java.util.List;

public interface IProductDAO extends IGenericDAO<Product, Long> {
    PageResult<Product> find(List<Category> categories, String categoryDescription, String productDescription, int page, int size);
    List<Product> findProducts(List<Product> products);
}
