package org.anasantana.services.impl;

import jakarta.transaction.Transactional;
import org.anasantana.daos.generics.IProductRestockingDAO;
import org.anasantana.daos.generics.IStockDAO;
import org.anasantana.dtos.ProductRestockingDTO;
import org.anasantana.model.Product;
import org.anasantana.model.ProductRestocking;
import org.anasantana.model.Stock;
import org.anasantana.services.IProductRestockingService;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.EntityNotFoundException;
import org.anasantana.services.exceptions.ResourceNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ProductRestockingServiceImpl implements IProductRestockingService {

    private final IProductRestockingDAO restockingRepository;
    private final IStockDAO stockRepository;
    private final StockService stockService;
    public ProductRestockingServiceImpl(IProductRestockingDAO restockingRepository,
                                        IStockDAO stockRepository, StockService stockService) {
        this.restockingRepository = restockingRepository;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
    }

    @Override
    @Transactional
    public List<ProductRestockingDTO> findAll() {
        return restockingRepository.findAll()
                .stream()
                .map(ProductRestockingDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductRestockingDTO findById(Long id) {
        Optional<ProductRestocking> obj = restockingRepository.findById(id);
        ProductRestocking entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));
        return new ProductRestockingDTO(entity);
    }

    @Override
    @Transactional
    public ProductRestockingDTO insert(ProductRestockingDTO dto) {
        ProductRestocking entity = new ProductRestocking();
        copyDtoToEntity(dto, entity);
        Product product = entity.getStock().getProduct();
        stockService.increaseStock(product, dto.getQuantity());
        entity.setMoment(Instant.now());
        entity = restockingRepository.save(entity);
        return new ProductRestockingDTO(entity);
    }


    @Override
    @Transactional
    public ProductRestockingDTO update(Long id, ProductRestockingDTO dto) {
        try {
            ProductRestocking entity = restockingRepository.getOne(id);
            int previousQuantity = entity.getQuantity();
            Product previousProduct = entity.getStock().getProduct();
            copyDtoToEntity(dto, entity);
            int newQuantity = entity.getQuantity();
            Product newProduct = entity.getStock().getProduct();
            stockService.decreaseStock(previousProduct, previousQuantity);
            stockService.increaseStock(newProduct, newQuantity);
            entity.setMoment(Instant.now());
            entity = restockingRepository.save(entity);
            return new ProductRestockingDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }


    @Override
    public void delete(Long id) {
        if (restockingRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Id not found " + id);
        }

        try {
            restockingRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBaseException("Integrity violation: " + e.getMessage(), e);
        }
    }
    private void copyDtoToEntity(ProductRestockingDTO dto, ProductRestocking entity) {
        entity.setQuantity(dto.getQuantity());

        if (dto.getStockId() != null) {
            Stock stock = stockRepository.getOne(dto.getStockId());
            entity.setStock(stock);
        } else {
            entity.setStock(null);
        }
    }
}
