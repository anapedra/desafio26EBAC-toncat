package org.anasantana.services;

import org.anasantana.daos.generics.IOrderDAO;
import org.anasantana.daos.generics.IOrderItemDAO;
import org.anasantana.daos.generics.IProductDAO;
import org.anasantana.daos.generics.IUserDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.daos.pagination.Pageable;
import org.anasantana.dtos.ClientDTO;
import org.anasantana.dtos.OrderDTO;
import org.anasantana.dtos.OrderItemDTO;
import org.anasantana.model.Order;
import org.anasantana.model.OrderItem;
import org.anasantana.model.Product;
import org.anasantana.model.User;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.EntityNotFoundException;
import org.anasantana.services.exceptions.ResourceNotFoundException;
import org.anasantana.services.impl.OrderServiceImpl;
import org.anasantana.utils.PageableMock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private IOrderDAO orderDAO;
    @Mock private IUserDAO userDAO;
    @Mock private IOrderItemDAO orderItemDAO;
    @Mock private IProductDAO productDAO;

    @InjectMocks private OrderServiceImpl orderService;

    private Order order;
    private User client;
    private Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(1L);
        product.setInitialPrice(100.0);
        product.setShortDescription("Produto");

        client = new User();
        client.setId(1L);
        client.setName("Ana");
        client.setCpf("123");

        order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setItems(Set.of(new OrderItem(order, product, 2, product.getInitialPrice())));
        order.setDateOrder(LocalDate.now());
    }

    @Test
    void testInsertSuccess() {
        ClientDTO clientDTO = new ClientDTO(client);
        OrderItemDTO itemDTO = new OrderItemDTO(product.getId(), 2);
        OrderDTO dto = new OrderDTO();
        dto.setClient(clientDTO);
        dto.setItems(List.of(itemDTO));

        when(productDAO.getOne(1L)).thenReturn(product);
        when(orderDAO.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.insert(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderItemDAO).saveAll(anyList());
    }

    @Test
    void testFindByIdSuccess() {
        when(orderDAO.findById(1L)).thenReturn(Optional.of(order));
        OrderDTO result = orderService.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(orderDAO.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.findById(99L));
    }

    @Test
    void testFindPagedWithFilters() {
        Pageable pageable = new PageableMock(0, 10);
        PageResult<Order> page = new PageResult<>(List.of(order), 0, 10, 1L);

        when(userDAO.getOne(1L)).thenReturn(client);
        when(orderDAO.find(client, "Ana", "123", LocalDate.now().minusDays(365), LocalDate.now(), 0, 10)).thenReturn(page);
        when(orderDAO.findOrder(List.of(order))).thenReturn(List.of(order));

        PageResult<OrderDTO> result = orderService.find(1L, "Ana", "123", null, null, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
    }



    @Test
    void testUpdateNotFound() {
        when(orderDAO.getOne(999L)).thenThrow(new EntityNotFoundException("not found"));
        OrderDTO dto = new OrderDTO();
        dto.setClient(new ClientDTO());
        dto.setItems(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> orderService.update(999L, dto));
    }

    @Test
    void testDeleteSuccess() {
        when(orderDAO.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderDAO).deleteById(1L);
        assertDoesNotThrow(() -> orderService.delete(1L));
    }

    @Test
    void testDeleteNotFound() {
        when(orderDAO.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.delete(999L));
    }

    @Test
    void testDeleteThrowsDataBaseException() {
        when(orderDAO.findById(1L)).thenReturn(Optional.of(order));
        doThrow(new RuntimeException("fk_violation")).when(orderDAO).deleteById(1L);
        assertThrows(DataBaseException.class, () -> orderService.delete(1L));
    }
}
