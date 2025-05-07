package org.anasantana.services;


import org.anasantana.dtos.ProductRestockingDTO;

import java.util.List;

public interface IProductRestockingService {

    List<ProductRestockingDTO> findAll();

    ProductRestockingDTO findById(Long id);

    ProductRestockingDTO insert(ProductRestockingDTO dto);

    ProductRestockingDTO update(Long id, ProductRestockingDTO dto);

    void delete(Long id);
}
