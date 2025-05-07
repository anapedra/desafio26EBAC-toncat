package org.anasantana.services;

import org.anasantana.daos.generics.ICategoryDAO;
import org.anasantana.dtos.CategoryDTO;
import org.anasantana.model.Category;
import org.anasantana.services.exceptions.ResourceNotFoundException;
import org.anasantana.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private ICategoryDAO categoryDAO;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private AutoCloseable closeable;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setDescription("Livros");
        category.setMomentRegistration(Instant.now());

        categoryDTO = new CategoryDTO(category);
    }

    @Test
    void testFindAll() {
        when(categoryDAO.findAll()).thenReturn(List.of(category));
        List<CategoryDTO> result = categoryService.findAll();
        assertEquals(1, result.size());
        assertEquals("Livros", result.get(0).getDescription());
    }

    @Test
    void testFindByIdFound() {
        when(categoryDAO.findById(1L)).thenReturn(Optional.of(category));
        CategoryDTO result = categoryService.findById(1L);
        assertEquals("Livros", result.getDescription());
    }

    @Test
    void testFindByIdNotFound() {
        when(categoryDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    void testInsert() {
        when(categoryDAO.save(any(Category.class))).thenAnswer(i -> {
            Category c = i.getArgument(0);
            c.setId(1L);
            return c;
        });

        CategoryDTO result = categoryService.insert(categoryDTO);
        assertNotNull(result);
        assertEquals("Livros", result.getDescription());
    }

    @Test
    void testUpdate() {
        when(categoryDAO.getOne(1L)).thenReturn(category);
        when(categoryDAO.save(any(Category.class))).thenReturn(category);

        CategoryDTO updated = new CategoryDTO();
        updated.setDescription("Atualizado");

        CategoryDTO result = categoryService.update(1L, updated);
        assertEquals("Atualizado", result.getDescription());
    }

    @Test
    void testUpdateNotFound() {
        when(categoryDAO.getOne(1L)).thenThrow(new org.anasantana.services.exceptions.EntityNotFoundException("not found"));
        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(1L, categoryDTO));
    }

    @Test
    void testDeleteSuccess() {
        when(categoryDAO.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryDAO).deleteById(1L);
        assertDoesNotThrow(() -> categoryService.delete(1L));
    }

    @Test
    void testDeleteNotFound() {
        when(categoryDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(1L));
    }

    @Test
    void testDeleteThrowsDataBaseException() {
        when(categoryDAO.findById(1L)).thenReturn(Optional.of(category));
        doThrow(new RuntimeException("fk_violation")).when(categoryDAO).deleteById(1L);
        assertThrows(org.anasantana.services.exceptions.DataBaseException.class, () -> categoryService.delete(1L));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
