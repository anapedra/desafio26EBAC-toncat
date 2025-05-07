package org.anasantana.services.impl;


import jakarta.transaction.Transactional;
import org.anasantana.daos.generics.ICategoryDAO;
import org.anasantana.dtos.CategoryDTO;
import org.anasantana.model.Category;
import org.anasantana.services.ICategoryService;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.EntityNotFoundException;
import org.anasantana.services.exceptions.ResourceNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryDAO categoryRepository;
    public CategoryServiceImpl(ICategoryDAO categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public List<CategoryDTO> findAll() {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));
        return new CategoryDTO(entity);
    }

    @Override
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setMomentRegistration(Instant.now());
        copyDtoToEntity(dto, entity);
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Override
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = categoryRepository.getOne(id);
            entity.setMomentUpdate(Instant.now());
            copyDtoToEntity(dto, entity);
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
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


    private void copyDtoToEntity(CategoryDTO dto, Category entity) {
        entity.setDescription(dto.getDescription());
    }
}
