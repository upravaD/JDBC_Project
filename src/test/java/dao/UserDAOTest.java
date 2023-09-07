package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.dao.RoleDAO;
import org.project.dao.UserDAO;
import org.project.model.Role;
import org.project.model.User;
import org.project.util.PostgresConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import util.PostgresContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
class UserDAOTest {
    @Container
    private static PostgreSQLContainer<?> postgres = PostgresContainer.getContainer();
    private Connection connection;
    private UserDAO userDAO;
    private RoleDAO roleDAO;

    @BeforeEach
    public void setup() {
        postgres.start();
        connection = PostgresConnection.getConnection(
                postgres.getDatabaseName(),
                postgres.getUsername(),
                postgres.getPassword());
        userDAO = new UserDAO(connection);
        roleDAO = new RoleDAO(connection);
    }

    @AfterEach
    public void cleanup() throws SQLException {
        connection.close();
        postgres.stop();
    }

    @Test
    void testCreateUser() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        User user = new User();
        user.setUsername("TestUser");
        user.setRole(role);

        // Test
        userDAO.create(user);

        // Verify
        User retrievedUser = userDAO.findByID(user.getId());
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(user.getUsername(), retrievedUser.getUsername());
        Assertions.assertEquals(user.getRole().getId(), retrievedUser.getRole().getId());

        // Cleanup
        userDAO.delete(user);
        roleDAO.delete(role);
    }

    @Test
    void testGetAllUsers() {
        // Setup
        List<User> beforeUsers = userDAO.getALL();

        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        User user1 = new User();
        user1.setUsername("TestUser1");
        user1.setRole(role);
        userDAO.create(user1);

        User user2 = new User();
        user2.setUsername("TestUser2");
        user2.setRole(role);
        userDAO.create(user2);

        // Test
        List<User> users = userDAO.getALL();

        // Verify
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(beforeUsers.size() + 2, users.size());
        User retrievedUser1 = users.get(users.size()-2);
        User retrievedUser2 = users.get(users.size()-1);
        Assertions.assertEquals(user1.getUsername(), retrievedUser1.getUsername());
        Assertions.assertEquals(user1.getRole().getId(), retrievedUser1.getRole().getId());
        Assertions.assertEquals(user2.getUsername(), retrievedUser2.getUsername());
        Assertions.assertEquals(user2.getRole().getId(), retrievedUser2.getRole().getId());

        // Cleanup
        userDAO.delete(user1);
        userDAO.delete(user2);
        roleDAO.delete(role);
    }

    @Test
    void testFindUserByID() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        User user = new User();
        user.setUsername("TestUser");
        user.setRole(role);
        userDAO.create(user);

        // Test
        User retrievedUser = userDAO.findByID(user.getId());

        // Verify
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(user.getId(), retrievedUser.getId());
        Assertions.assertEquals(user.getUsername(), retrievedUser.getUsername());
        Assertions.assertEquals(user.getRole().getId(), retrievedUser.getRole().getId());

        // Cleanup
        userDAO.delete(user);
        roleDAO.delete(role);
    }

    @Test
    void testFindNonExistentUserByID() {
        // Test
        User retrievedUser = userDAO.findByID(-1L);

        // Verify
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(-1L, retrievedUser.getId());
        Assertions.assertEquals("emptyUserName", retrievedUser.getUsername());
        Assertions.assertNotNull(retrievedUser.getRole());
        Assertions.assertEquals(-1L, retrievedUser.getRole().getId());
    }

    @Test
    void testUpdateUser() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        User user = new User();
        user.setUsername("OldUser");
        user.setRole(role);
        userDAO.create(user);

        User updatedUser = userDAO.findByID(user.getId());
        updatedUser.setUsername("NewUser");
        updatedUser.setRole(role);

        // Test
        userDAO.update(updatedUser);

        // Verify
        User retrievedUser = userDAO.findByID(user.getId());
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(updatedUser.getUsername(), retrievedUser.getUsername());
        Assertions.assertEquals(updatedUser.getRole().getId(), retrievedUser.getRole().getId());

        // Cleanup
        userDAO.delete(updatedUser);
        roleDAO.delete(role);
    }

    @Test
    void testDeleteUser() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        User user = new User();
        user.setUsername("TestUser");
        user.setRole(role);
        userDAO.create(user);

        // Test
        userDAO.delete(user);

        // Verify
        User deletedUser = userDAO.findByID(user.getId());
        Assertions.assertNotNull(deletedUser);
        Assertions.assertEquals(-1L, deletedUser.getId());
        Assertions.assertEquals("emptyUserName", deletedUser.getUsername());
        Assertions.assertNotNull(deletedUser.getRole());
        Assertions.assertEquals(-1L, deletedUser.getRole().getId());

        // Cleanup
        roleDAO.delete(role);
    }
}