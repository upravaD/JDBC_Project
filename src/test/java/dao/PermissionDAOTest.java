package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.project.dao.PermissionDAO;
import org.project.model.Permission;
import org.project.util.PostgresConnection;
import org.project.util.PostgresPropertiesReader;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
class PermissionDAOTest {
    private static final PostgresPropertiesReader propertiesReader = new PostgresPropertiesReader();
    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName(propertiesReader.getUrl())
            .withUsername(propertiesReader.getUser())
            .withPassword(propertiesReader.getPassword());
    private Connection connection;
    private PermissionDAO permissionDAO;

    @BeforeEach
    public void setup() {
        postgres.start();
        connection = PostgresConnection.getConnection(
                postgres.getDatabaseName(),
                postgres.getUsername(),
                postgres.getPassword());
        permissionDAO = new PermissionDAO(connection);
    }

    @AfterEach
    public void cleanup() throws SQLException {
        connection.close();
        postgres.stop();
    }

    @Test
    void testCreatePermission() {
        // Setup
        String name = "TestCreatePermission";
        Permission permission = new Permission();
        permission.setPermissionName(name);

        // Test
        permissionDAO.create(permission);

        // Verify
        Permission createdPermission = permissionDAO.findByID(permission.getId());
        Assertions.assertNotNull(createdPermission);
        Assertions.assertEquals(name, createdPermission.getPermissionName());
        Assertions.assertEquals(permission.getId(), createdPermission.getId());

        // Cleanup
        permissionDAO.delete(permission);
    }

    @Test
    void testGetAllPermissions() {
        // Setup
        String name1 = "TestGetAllPermission1";
        String name2 = "TestGetAllPermission2";
        List<Permission> beforePermissions = permissionDAO.getALL();
        Permission permission1 = new Permission();
        permission1.setPermissionName(name1);
        Permission permission2 = new Permission();
        permission2.setPermissionName(name2);
        permissionDAO.create(permission1);
        permissionDAO.create(permission2);

        // Test
        List<Permission> permissions = permissionDAO.getALL();

        // Verify
        Assertions.assertNotNull(permissions);
        Assertions.assertEquals(beforePermissions.size() + 2, permissions.size());

        // Cleanup
        permissionDAO.delete(permission1);
        permissionDAO.delete(permission2);
    }

    @Test
    void testFindPermissionByID() {
        // Setup
        Permission permission = new Permission();
        permission.setPermissionName("TestFindByIDPermission");
        permissionDAO.create(permission);

        // Test
        Permission foundPermission = permissionDAO.findByID(permission.getId());

        // Verify
        Assertions.assertNotNull(foundPermission);
        Assertions.assertEquals(permission.getId(), foundPermission.getId());
        Assertions.assertEquals(permission.getPermissionName(), foundPermission.getPermissionName());

        // Cleanup
        permissionDAO.delete(permission);
    }

    @Test
    void testFindNonExistentPermission() {
        // Test
        Permission nonExistentPermission = permissionDAO.findByID(-1L);

        // Verify
        Assertions.assertNotNull(nonExistentPermission);
        Assertions.assertEquals(-1L, nonExistentPermission.getId());
        Assertions.assertEquals("emptyPermissionName", nonExistentPermission.getPermissionName());
    }

    @Test
    void testUpdatePermission() {
        // Setup
        Permission permission = new Permission();
        permission.setPermissionName("TestOldPermission");
        permissionDAO.create(permission);

        Permission updatedPermission = new Permission();
        updatedPermission.setId(permission.getId());
        updatedPermission.setPermissionName("TestNewPermission");

        // Test
        permissionDAO.update(updatedPermission);

        // Verify
        Permission retrievedPermission = permissionDAO.findByID(permission.getId());
        Assertions.assertNotNull(retrievedPermission);
        Assertions.assertEquals(updatedPermission.getPermissionName(), retrievedPermission.getPermissionName());

        // Cleanup
        permissionDAO.delete(updatedPermission);
    }

    @Test
    void testDeletePermission() {
        // Setup
        Permission permission = new Permission();
        permission.setPermissionName("TestDeletePermission");
        permissionDAO.create(permission);

        // Test
        permissionDAO.delete(permission);

        // Verify
        Permission deletedPermission = permissionDAO.findByID(permission.getId());
        Assertions.assertNotNull(deletedPermission);
        Assertions.assertEquals(-1L, deletedPermission.getId());
        Assertions.assertEquals("emptyPermissionName", deletedPermission.getPermissionName());
    }
}
