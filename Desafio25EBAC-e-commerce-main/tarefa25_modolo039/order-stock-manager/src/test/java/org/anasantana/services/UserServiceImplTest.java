package org.anasantana.services;

import org.anasantana.daos.generics.IUserDAO;
import org.anasantana.daos.pagination.PageResult;
import org.anasantana.dtos.ClientDTO;
import org.anasantana.model.User;
import org.anasantana.services.exceptions.DataBaseException;
import org.anasantana.services.exceptions.ResourceNotFoundException;
import org.anasantana.services.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private IUserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;

    private User user;
    private ClientDTO userDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Ana Santana");
        user.setCpf("12345678901");
        user.setMainPhone("31999999999");
        user.setMomentRegistration(Instant.now());
        user.setMomentUpdate(Instant.now());
        user.setRegistrationEmail("ana@example.com");

        userDTO = new ClientDTO(user);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testFindAll() {
        PageResult<User> page = new PageResult<>(List.of(user), 0, 1, 1L);
        when(userDAO.findAllPaged(0, 1)).thenReturn(page);

        PageResult<ClientDTO> result = userService.findAll(0, 1);
        assertEquals(1, result.getContent().size());
        assertEquals("Ana Santana", result.getContent().get(0).getName());
    }

    @Test
    void testFindByIdFound() {
        when(userDAO.findById(1L)).thenReturn(Optional.of(user));

        ClientDTO result = userService.findById(1L);
        assertNotNull(result);
        assertEquals("Ana Santana", result.getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(userDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testInsert() {
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        ClientDTO result = userService.insert(userDTO);
        assertNotNull(result);
        assertEquals("Ana Santana", result.getName());
    }

    @Test
    void testUpdateSuccess() {
        when(userDAO.getOne(1L)).thenReturn(user);
        when(userDAO.save(any(User.class))).thenReturn(user);

        ClientDTO dto = new ClientDTO(user);
        dto.setName("Atualizado");

        ClientDTO result = userService.update(1L, dto);
        assertEquals("Atualizado", result.getName());
    }

    @Test
    void testUpdateNotFound() {
        when(userDAO.getOne(1L)).thenThrow(new RuntimeException("not found"));

        assertThrows(ResourceNotFoundException.class, () -> userService.update(1L, userDTO));
    }

    @Test
    void testDeleteSuccess() {
        when(userDAO.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userDAO).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteById(1L));
    }

    @Test
    void testDeleteNotFound() {
        when(userDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(1L));
    }

    @Test
    void testDeleteThrowsDataBaseException() {
        when(userDAO.findById(1L)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("FK violation")).when(userDAO).deleteById(1L);

        assertThrows(DataBaseException.class, () -> userService.deleteById(1L));
    }

    @Test
    void testGetProfileNotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> userService.getProfile());
    }
}
