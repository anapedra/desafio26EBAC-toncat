package org.anasantana.services;

import org.anasantana.daos.pagination.PageResult;
import org.anasantana.daos.pagination.Pageable;
import org.anasantana.dtos.ProductDTO;

public interface IProductService {

    PageResult<ProductDTO> find(Long categoryId, String descriptionCategory, String descriptionProduct, Pageable pageable);

    ProductDTO findById(Long id);
    ProductDTO insert(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
}
