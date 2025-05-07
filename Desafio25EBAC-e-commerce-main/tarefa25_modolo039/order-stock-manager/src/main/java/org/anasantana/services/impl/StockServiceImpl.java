package org.anasantana.services.impl;


import jakarta.transaction.Transactional;
import org.anasantana.daos.generics.IProductDAO;
import org.anasantana.daos.generics.IStockDAO;
import org.anasantana.dtos.StockDTO;
import org.anasantana.model.Product;
import org.anasantana.model.Stock;
import org.anasantana.services.IStockService;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.EntityNotFoundException;
import org.anasantana.services.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class StockServiceImpl implements IStockService {

    private final IStockDAO stockRepository;
    private final IProductDAO productRepository;
    public StockServiceImpl(IStockDAO stockRepository, IProductDAO productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public List<StockDTO> findAll() {
        return stockRepository.findAll()
                .stream()
                .map(StockDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockDTO findById(Long id) {
        Optional<Stock> obj = stockRepository.findById(id);
        Stock entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));
        return new StockDTO(entity);
    }

    @Override
    @Transactional
    public StockDTO insert(StockDTO dto) {
        Stock entity = new Stock();
        copyDtoToEntity(dto, entity);
        entity = stockRepository.save(entity);
        return new StockDTO(entity);
    }

    @Override
    @Transactional
    public StockDTO update(Long id, StockDTO dto) {
        try {
            Stock entity = stockRepository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = stockRepository.save(entity);
            return new StockDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }

    @Override
    public void delete(Long id) {
        if (stockRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Id not found " + id);
        }

        try {
            stockRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBaseException("Integrity violation: " + e.getMessage(), e);
        }
    }

    private void copyDtoToEntity(StockDTO dto, Stock entity) {
        entity.setQuantity(dto.getQuantity());
        entity.setStockStatus(dto.getStockStatus());

        if (dto.getProductId() != null) {
            Product product = productRepository.getOne(dto.getProductId());
            entity.setProduct(product);
        } else {
            entity.setProduct(null);
        }
    }
}
