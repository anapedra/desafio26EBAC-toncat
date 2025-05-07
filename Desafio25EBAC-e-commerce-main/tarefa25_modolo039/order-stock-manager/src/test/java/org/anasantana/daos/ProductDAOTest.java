package org.anasantana.daos;

import jakarta.persistence.*;
import org.anasantana.daos.generics.impl.ProductDAOImpl;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.model.Category;
import org.anasantana.model.Product;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private ProductDAOImpl productDAO;

    @BeforeAll
    static void setupClass() {
        emf = Persistence.createEntityManagerFactory("main-pu");
    }

    @BeforeEach
    void setup() {
        em = emf.createEntityManager();
        productDAO = new ProductDAOImpl(em);
        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    @AfterAll
    static void tearDownClass() {
        emf.close();
    }

    private Product createProduct(String desc, Category cat) {
        Product p = new Product();
        p.setShortDescription(desc);
        p.setFullDescription("Full " + desc);
        p.setProductCost(10.0);
        p.setInitialPrice(20.0);
        p.setMomentRegistration(Instant.now());
        p.setMomentUpdate(Instant.now());
        p.getCategories().add(cat);
        cat.getProducts().add(p);
        em.persist(cat);
        return p;
    }

    @Test
    void testFind_withAllFilters() {
        Category cat = new Category();
        cat.setDescription("Tech");
        Product p = createProduct("Laptop", cat);
        em.persist(p);
        em.flush();

        PageResult<Product> result = productDAO.find(List.of(cat), "Tech", "Laptop", 0, 10);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    void testFind_withOnlyCategoryDescription() {
        Category cat = new Category();
        cat.setDescription("Books");
        Product p = createProduct("Book", cat);
        em.persist(p);
        em.flush();

        PageResult<Product> result = productDAO.find(null, "Books", null, 0, 10);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    void testFind_withOnlyProductDescription() {
        Category cat = new Category();
        cat.setDescription("Games");
        Product p = createProduct("Console", cat);
        em.persist(p);
        em.flush();

        PageResult<Product> result = productDAO.find(null, null, "Console", 0, 10);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    void testFind_withOnlyCategories() {
        Category cat = new Category();
        cat.setDescription("Appliances");
        Product p = createProduct("Fridge", cat);
        em.persist(p);
        em.flush();

        PageResult<Product> result = productDAO.find(List.of(cat), null, null, 0, 10);
        assertFalse(result.getContent().isEmpty());
    }



    @Test
    void testFindProducts_shouldReturnWithCategories() {
        Category cat = new Category();
        cat.setDescription("Smartphones");
        Product p = createProduct("iPhone", cat);
        em.persist(p);
        em.flush();

        List<Product> result = productDAO.findProducts(List.of(p));
        assertFalse(result.isEmpty());
        assertEquals("iPhone", result.get(0).getShortDescription());
        assertFalse(result.get(0).getCategories().isEmpty());
    }

    @Test
    void testFindProducts_withEmptyList() {
        List<Product> result = productDAO.findProducts(Collections.emptyList());
        assertTrue(result.isEmpty());
    }


}
