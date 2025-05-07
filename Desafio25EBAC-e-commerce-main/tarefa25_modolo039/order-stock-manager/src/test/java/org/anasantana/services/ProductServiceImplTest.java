package org.anasantana.services;

import org.anasantana.daos.generics.ICategoryDAO;
import org.anasantana.daos.generics.IProductDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.daos.pagination.Pageable;
import org.anasantana.daos.pagination.PageableMock;
import org.anasantana.dtos.CategoryDTO;
import org.anasantana.dtos.ProductDTO;
import org.anasantana.model.Category;
import org.anasantana.model.Product;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.EntityNotFoundException;
import org.anasantana.services.exceptions.ResourceNotFoundException;
import org.anasantana.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private IProductDAO productDAO;

    @Mock
    private ICategoryDAO categoryDAO;

    @InjectMocks
    private ProductServiceImpl productService;

    private AutoCloseable closeable;

    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setDescription("Eletrônicos");

        product = new Product();
        product.setId(1L);
        product.setShortDescription("Fone");
        product.setFullDescription("Fone com redução de ruído");
        product.setInitialPrice(199.90);
        product.setMomentRegistration(Instant.now());
        product.setMomentUpdate(Instant.now());
        product.setProductCost(150.00);
        product.setImgUrl("http://img.com/fone.jpg");
        product.getCategories().add(category);

        productDTO = new ProductDTO(product);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testFindByIdFound() {
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));
        ProductDTO result = productService.findById(1L);
        assertEquals("Fone", result.getShortDescription());
    }

    @Test
    void testFindByIdNotFound() {
        when(productDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void testInsert() {
        when(categoryDAO.getOne(1L)).thenReturn(category);
        when(productDAO.save(any(Product.class))).thenAnswer(i -> {
            Product p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProductDTO dto = new ProductDTO();
        dto.setShortDescription("Novo Produto");
        dto.setInitialPrice(100.0);
        dto.setFullDescription("Desc");
        dto.setImgUrl("url");
        dto.setCategories(List.of(new CategoryDTO(category)));

        ProductDTO result = productService.insert(dto);
        assertNotNull(result);
        assertEquals("Novo Produto", result.getShortDescription());
    }

    @Test
    void testUpdateSuccess() {
        when(productDAO.getOne(1L)).thenReturn(product);
        when(categoryDAO.getOne(1L)).thenReturn(category);
        when(productDAO.save(any(Product.class))).thenReturn(product);

        ProductDTO dto = new ProductDTO();
        dto.setShortDescription("Atualizado");
        dto.setInitialPrice(88.0);
        dto.setCategories(List.of(new CategoryDTO(category)));

        ProductDTO result = productService.update(1L, dto);
        assertEquals("Atualizado", result.getShortDescription());
    }

    @Test
    void testUpdateNotFound() {
        when(productDAO.getOne(1L)).thenThrow(new EntityNotFoundException("not found"));

        assertThrows(ResourceNotFoundException.class, () -> productService.update(1L, productDTO));
    }

    @Test
    void testDeleteSuccess() {
        when(categoryDAO.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryDAO).deleteById(1L);
        assertDoesNotThrow(() -> productService.delete(1L));
    }

    @Test
    void testDeleteNotFound() {
        when(categoryDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(1L));
    }

    @Test
    void testDeleteThrowsDataBaseException() {
        when(categoryDAO.findById(1L)).thenReturn(Optional.of(category));
        doThrow(new RuntimeException("erro")).when(categoryDAO).deleteById(1L);
        assertThrows(DataBaseException.class, () -> productService.delete(1L));
    }
    @Test
    void testFindWithFilters() {
        Pageable pageable = new PageableMock(0, 10);

        when(categoryDAO.getOne(1L)).thenReturn(category);

        List<Product> products = List.of(product);
        PageResult<Product> pageResult = new PageResult<>(products, 0, 10, 1L);

        when(productDAO.find(List.of(category), "Eletrônicos", "Fone", 0, 10)).thenReturn(pageResult);
        when(productDAO.findProducts(products)).thenReturn(products); // Aqui está o fix 🔧

        PageResult<ProductDTO> result = productService.find(1L, "Eletrônicos", "Fone", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Fone", result.getContent().get(0).getShortDescription());
    }

}
