package service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.project.dao.UserDAO;
import org.project.model.User;
import org.project.service.UserService;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import util.PostgresContainer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Testcontainers
public class UserServiceTest {
    @Container
    private static PostgreSQLContainer<?> postgres = PostgresContainer.getContainer();
    @Mock
    private UserDAO mockUserDAO;
    private UserService userService;

    @Before
    public void setUp() {
        postgres.start();
        MockitoAnnotations.openMocks(this);
        userService = new UserService();
        userService.setUserDAO(mockUserDAO);
    }

    @After
    public void cleanUp() {
        postgres.stop();
    }

    @Test
    public void testAddUser() {
        User userToAdd = new User();
        userToAdd.setId(1L);
        userToAdd.setUsername("TestUser");

        when(mockUserDAO.create(userToAdd)).thenReturn(true);
        when(mockUserDAO.findByID(anyLong())).thenReturn(userToAdd);

        User addedUser = userService.add(userToAdd);

        assertEquals("TestUser", addedUser.getUsername());
    }

    @Test
    public void testReadUser() {
        User userToRead = new User();
        userToRead.setId(1L);
        userToRead.setUsername("TestUser");

        when(mockUserDAO.findByID(1L)).thenReturn(userToRead);

        User readUser = userService.read(1L);

        assertEquals("TestUser", readUser.getUsername());
    }

    @Test
    public void testReadAllUsers() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("User1");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("User2");
        users.add(user1);
        users.add(user2);

        when(mockUserDAO.getALL()).thenReturn(users);

        List<User> allUsers = userService.readAll();

        assertEquals(2, allUsers.size());
        assertEquals("User1", allUsers.get(0).getUsername());
        assertEquals("User2", allUsers.get(1).getUsername());
    }

    @Test
    public void testUpdateUser() {
        User userToUpdate = new User();
        userToUpdate.setId(1L);
        userToUpdate.setUsername("TestUser");

        when(mockUserDAO.update(userToUpdate)).thenReturn(true);

        boolean result = userService.update(userToUpdate);

        assertTrue(result);
    }

    @Test
    public void testRemoveUser() {
        User userToRemove = new User();
        userToRemove.setId(1L);

        when(mockUserDAO.delete(userToRemove)).thenReturn(true);

        boolean result = userService.remove(userToRemove);

        assertTrue(result);
    }
}