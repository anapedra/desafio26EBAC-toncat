package org.anasantana.daos;

import jakarta.persistence.*;
import org.anasantana.daos.generics.impl.OrderDAOImpl;
import org.anasantana.daos.generics.impl.UserDAOImpl;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.model.Order;
import org.anasantana.model.User;
import org.anasantana.model.enums.OrderStatus;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private OrderDAOImpl orderDAO;
    private UserDAOImpl userDAO;

    @BeforeAll
    static void setupClass() {
        emf = Persistence.createEntityManagerFactory("main-pu");
    }

    @BeforeEach
    void setup() {
        em = emf.createEntityManager();
        orderDAO = new OrderDAOImpl(em);
        userDAO = new UserDAOImpl(em);
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

    private User createSampleUser(String email) {
        User user = new User();
        user.setName("Ana Order");
        user.setRegistrationEmail(email);
        user.setCpf("321.654.987-00");
        user.setPassword("senha123");
        user.setMainPhone("31988888888");
        user.setMomentRegistration(Instant.now());
        user.setMomentUpdate(Instant.now());
        return userDAO.save(user);
    }

    private Order createSampleOrder(User client, LocalDate date) {
        Order order = new Order();
        order.setClient(client);
        order.setDateOrder(date);
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.PAID);
        return orderDAO.save(order);
    }

    @Test
    void testSaveAndFindById() {
        User user = createSampleUser("order" + UUID.randomUUID() + "@example.com");
        Order order = createSampleOrder(user, LocalDate.now());
        em.flush();

        Optional<Order> found = orderDAO.findById(order.getId());
        assertTrue(found.isPresent());
        assertEquals(order.getClient().getId(), found.get().getClient().getId());
    }

    @Test
    void testFind_withFilters() {
        User user = createSampleUser("filter" + UUID.randomUUID() + "@example.com");
        createSampleOrder(user, LocalDate.now().minusDays(1));
        em.flush();

        PageResult<Order> result = orderDAO.find(
                user,
                "Ana",
                "321.654.987-00",
                LocalDate.now().minusDays(30),
                LocalDate.now().plusDays(30),
                0, 10
        );

        assertTrue(result.getContent().size() >= 1);
    }

    @Test
    void testFindOrder() {
        User user = createSampleUser("fetch" + UUID.randomUUID() + "@example.com");
        Order order = createSampleOrder(user, LocalDate.now());
        em.flush();

        List<Order> result = orderDAO.findOrder(List.of(order));
        assertFalse(result.isEmpty());
        assertEquals(order.getId(), result.get(0).getId());
        assertNotNull(result.get(0).getClient());
    }

    @Test
    void testFindSummary() {
        User user = createSampleUser("summary" + UUID.randomUUID() + "@example.com");
        createSampleOrder(user, LocalDate.now());
        em.flush();

        List<Order> result = orderDAO.findSummary(LocalDate.now().minusDays(30), LocalDate.now().plusDays(30));
        assertFalse(result.isEmpty());
    }

    @Test
    void testSumSale() {
        User user = createSampleUser("sum" + UUID.randomUUID() + "@example.com");
        createSampleOrder(user, LocalDate.now());
        em.flush();

        List<Order> result = orderDAO.sumSale(LocalDate.now().minusDays(30), LocalDate.now().plusDays(30));
        assertFalse(result.isEmpty());
    }
}
