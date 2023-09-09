package dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import org.project.dao.PermissionDAO;
import org.project.dao.RoleDAO;
import org.project.model.Permission;
import org.project.model.Role;
import org.project.util.PostgresConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import util.PostgresContainer;

import java.util.List;

import static org.junit.Assert.*;

@Testcontainers
public class PermissionDAOTest {
    @Container
    private static PostgreSQLContainer<?> postgres = PostgresContainer.getContainer();
    private PermissionDAO permissionDAO;

    @Before
    public void setup() {
        postgres.start();
        PostgresConnection.setParameters(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        permissionDAO = new PermissionDAO();
    }

    @After
    public void cleanup() {
        postgres.stop();
    }

    @Test
    public void testCreatePermission() {
        // Setup
        String name = "TestCreatePermission";
        Permission permission = new Permission();
        permission.setPermissionName(name);

        // Test
        permissionDAO.create(permission);

        // Verify
        Permission createdPermission = permissionDAO.findByID(permission.getId());
        assertNotNull(createdPermission);
        assertEquals(name, createdPermission.getPermissionName());
        assertEquals(permission.getId(), createdPermission.getId());

        // Cleanup
        permissionDAO.delete(permission);
    }

    @Test
    public void testGetAllPermissions() {
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
        assertNotNull(permissions);
        assertEquals(beforePermissions.size() + 2, permissions.size());

        // Cleanup
        permissionDAO.delete(permission1);
        permissionDAO.delete(permission2);
    }

    @Test
    public void testFindPermissionByID() {
        // Setup
        Permission permission = new Permission();
        permission.setPermissionName("TestFindByIDPermission");
        permissionDAO.create(permission);

        // Test
        Permission foundPermission = permissionDAO.findByID(permission.getId());

        // Verify
        assertNotNull(foundPermission);
        assertEquals(permission.getId(), foundPermission.getId());
        assertEquals(permission.getPermissionName(), foundPermission.getPermissionName());

        // Cleanup
        permissionDAO.delete(permission);
    }

    @Test
    public void testFindNonExistentPermission() {
        // Test
        Permission nonExistentPermission = permissionDAO.findByID(-1L);

        // Verify
        assertNotNull(nonExistentPermission);
        Assertions.assertEquals(-1L, nonExistentPermission.getId());
        assertEquals("emptyPermissionName", nonExistentPermission.getPermissionName());
    }

    @Test
    public void testUpdatePermission() {
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
        assertNotNull(retrievedPermission);
        assertEquals(updatedPermission.getPermissionName(), retrievedPermission.getPermissionName());

        // Cleanup
        permissionDAO.delete(updatedPermission);
    }

    @Test
    public void testDeletePermission() {
        // Setup
        Permission permission = new Permission();
        permission.setPermissionName("TestDeletePermission");
        permissionDAO.create(permission);

        // Test
        permissionDAO.delete(permission);

        // Verify
        Permission deletedPermission = permissionDAO.findByID(permission.getId());
        assertNotNull(deletedPermission);
        Assertions.assertEquals(-1L, deletedPermission.getId());
        assertEquals("emptyPermissionName", deletedPermission.getPermissionName());
    }

    @Test
    public void testGetRolesByPermission() {
        // Setup
        RoleDAO roleDAO = new RoleDAO();
        Permission permission = new Permission();
        permission.setPermissionName("TestRolesPermission");
        permissionDAO.create(permission);
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);
        roleDAO.setRolePermission(role, permission);

        // Test
        List<Role> roles = permissionDAO.getRolePermissionByID(permission);
        Permission expectedPermission = permissionDAO.findByID(permission.getId());
        expectedPermission.setRoles(roles);

        // Verify
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals("TestRolesPermission", expectedPermission.getPermissionName());
        assertEquals(roles.get(0), expectedPermission.getRoles().get(0));

        // Cleanup
        permissionDAO.deleteRolePermission(permission);
        permissionDAO.delete(permission);
        roleDAO.delete(role);
    }
}