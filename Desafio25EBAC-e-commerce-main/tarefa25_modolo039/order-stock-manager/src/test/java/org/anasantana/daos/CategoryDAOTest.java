package org.anasantana.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.anasantana.daos.generics.ICategoryDAO;
import org.anasantana.daos.generics.impl.CategoryDAOImpl;
import org.anasantana.model.Category;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryDAOTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private ICategoryDAO categoryDAO;

    @BeforeAll
    public static void setupClass() {
        emf = Persistence.createEntityManagerFactory("main-pu");
    }

    @BeforeEach
    public void setup() {
        em = emf.createEntityManager();
        categoryDAO = new CategoryDAOImpl(em);
        em.getTransaction().begin();
    }

    @AfterEach
    public void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    @AfterAll
    public static void tearDownClass() {
        emf.close();
    }

    @Test
    @Order(1)
    public void testSave() {
        Category category = new Category(null, "Testesssssssss Category", Instant.now(), Instant.now());
        Category saved = categoryDAO.save(category);
        assertNotNull(saved.getId());
    }

    @Test
    @Order(2)
    public void testFindById() {
        Optional<Category> opt = categoryDAO.findById(1L);
        assertTrue(opt.isPresent(), "Categoria com ID 1 deveria existir");
        assertEquals("Eletrônicos", opt.get().getDescription());
    }

    @Test
    @Order(3)
    public void testFindAll() {
        List<Category> all = categoryDAO.findAll();
        assertFalse(all.isEmpty(), "A lista de categorias não deveria estar vazia");
        assertTrue(all.size() >= 3, "Deveria haver ao menos 3 categorias");
    }

    @Test
    @Order(4)
    public void testFindByDescription() {
        List<Category> result = categoryDAO.findByDescription("eletr");
        assertFalse(result.isEmpty(), "Deveria encontrar categorias com 'eletr' no nome");
        for (Category c : result) {
            assertTrue(c.getDescription().toLowerCase().contains("eletr"));
        }
    }

    @Test
    @Order(5)
    public void testDeleteById() {
        Category category = new Category(null, "To Be Deleted", Instant.now(), Instant.now());
        category = categoryDAO.save(category);
        Long id = category.getId();
        categoryDAO.deleteById(id);
        assertTrue(categoryDAO.findById(id).isEmpty(), "Categoria deveria ter sido removida");
    }

    @Test
    @Order(6)
    public void testFindAllPaged() {
        var page = categoryDAO.findAllPaged(0, 2);
        assertNotNull(page);
        assertFalse(page.getContent().isEmpty());
        assertTrue(page.getContent().size() <= 2);
        assertTrue(page.getTotalElements() >= 3);
    }
}
