package org.anasantana.services;



import org.anasantana.dtos.StockDTO;

import java.util.List;

public interface IStockService {

    List<StockDTO> findAll();

    StockDTO findById(Long id);

    StockDTO insert(StockDTO dto);

    StockDTO update(Long id, StockDTO dto);

    void delete(Long id);
}
