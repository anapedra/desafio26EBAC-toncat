package org.anasantana.services.impl;

import jakarta.transaction.Transactional;
import org.anasantana.daos.generics.ICategoryDAO;
import org.anasantana.daos.generics.IProductDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.daos.pagination.Pageable;
import org.anasantana.dtos.CategoryDTO;
import org.anasantana.dtos.ProductDTO;
import org.anasantana.model.Category;
import org.anasantana.model.Product;
import org.anasantana.services.IProductService;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.EntityNotFoundException;
import org.anasantana.services.exceptions.ResourceNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements IProductService {

    private final IProductDAO repository;
    private final ICategoryDAO categoryRepository;

    public ProductServiceImpl(IProductDAO repository, ICategoryDAO categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public PageResult<ProductDTO> find(Long categoryId, String descriptionCategory, String descriptionProduct, Pageable pageable) {
        List<Category> categories = (categoryId == 0) ? null : List.of(categoryRepository.getOne(categoryId));
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        PageResult<Product> result = repository.find(categories, descriptionCategory, descriptionProduct, page, size);
        repository.findProducts(result.getContent());

        return new PageResult<>(
                result.getContent().stream().map(ProductDTO::new).toList(),
                result.getPage(),
                result.getSize(),
                result.getTotalElements()
        );
    }


    @Override
    @Transactional
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Override
    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Override
    public void delete(Long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBaseException("Integrity violation: " + e.getMessage(), e);
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setShortDescription(dto.getShortDescription());
        entity.setFullDescription(dto.getFullDescription());
        entity.setInitialPrice(dto.getInitialPrice());
        entity.setMomentRegistration(Instant.now());
        entity.setMomentUpdate(Instant.now());
        entity.setImgUrl(dto.getImgUrl());
        entity.setProductCost(dto.getInitialPrice());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}
