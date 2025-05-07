package org.anasantana.daos;

import org.anasantana.daos.pagination.PageResult;
import org.anasantana.model.User;
import org.anasantana.daos.generics.impl.UserDAOImpl;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private UserDAOImpl userDAO;

    @BeforeAll
    static void setupClass() {
        emf = Persistence.createEntityManagerFactory("main-pu");
    }

    @BeforeEach
    void setup() {
        em = emf.createEntityManager();
        userDAO = new UserDAOImpl(em);
        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback(); // Limpa estado após cada teste
        }
        em.close();
    }

    @AfterAll
    static void tearDownClass() {
        emf.close();
    }

    private User createSampleUser(String email) {
        User user = new User();
        user.setName("Ana Dev");
        user.setRegistrationEmail(email);
        user.setCpf("123.456.789-00");
        user.setPassword("secret");
        user.setMainPhone("31999999999");
        user.setMomentRegistration(Instant.now());
        user.setMomentUpdate(Instant.now());
        return user;
    }

    @Test
    void testFindByEmail_whenUserExists() {
        String email = "ana" + UUID.randomUUID() + "@example.com";
        User user = createSampleUser(email);
        userDAO.save(user);
        em.flush();

        Optional<User> result = userDAO.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getRegistrationEmail());
    }

    @Test
    void testFindByEmail_whenUserDoesNotExist() {
        Optional<User> result = userDAO.findByEmail("naoexiste@example.com");
        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        User user = createSampleUser("save" + UUID.randomUUID() + "@example.com");
        User saved = userDAO.save(user);
        em.flush();

        assertNotNull(saved.getId());
    }


    @Test
    void testDeleteById() {
        User user = createSampleUser("delete" + UUID.randomUUID() + "@example.com");
        user = userDAO.save(user);
        em.flush();

        Long id = user.getId();
        assertNotNull(id);

        userDAO.deleteById(id);
        em.flush();

        Optional<User> result = userDAO.findById(id);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll() {
        User u1 = createSampleUser("all1" + UUID.randomUUID() + "@example.com");
        User u2 = createSampleUser("all2" + UUID.randomUUID() + "@example.com");
        userDAO.save(u1);
        userDAO.save(u2);
        em.flush();

        List<User> all = userDAO.findAll();
        assertTrue(all.size() >= 2);
    }

    @Test
    void testFindAllPaged() {
        for (int i = 1; i <= 10; i++) {
            userDAO.save(createSampleUser("paged" + i + UUID.randomUUID() + "@example.com"));
        }
        em.flush();

        PageResult<User> pageResult = userDAO.findAllPaged(0, 5);

        assertEquals(5, pageResult.getContent().size());
        assertEquals(0, pageResult.getPage());
        assertEquals(5, pageResult.getSize());
        assertTrue(pageResult.getTotalElements() >= 10);
    }
}
