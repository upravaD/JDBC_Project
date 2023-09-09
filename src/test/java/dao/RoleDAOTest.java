package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

@Testcontainers
class RoleDAOTest {
    @Container
    private static PostgreSQLContainer<?> postgres = PostgresContainer.getContainer();
    private RoleDAO roleDAO;

    @BeforeEach
    public void setup() {
        postgres.start();
        PostgresConnection.setParameters(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
        roleDAO = new RoleDAO();
    }
    @AfterEach
    public void cleanup() {
        postgres.stop();
    }

    @Test
    void testCreateRole() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");

        // Test
        roleDAO.create(role);

        // Verify
        Role createdRole = roleDAO.findByID(role.getId());
        Assertions.assertNotNull(createdRole);
        Assertions.assertEquals(role.getRoleName(), createdRole.getRoleName());

        // Cleanup
        roleDAO.delete(role);
    }

    @Test
    void testGetAllRoles() {
        // Setup
        List<Role> beforeRoles = roleDAO.getALL();

        Role role1 = new Role();
        role1.setRoleName("Role1");
        roleDAO.create(role1);

        Role role2 = new Role();
        role2.setRoleName("Role2");
        roleDAO.create(role2);

        // Test
        List<Role> roles = roleDAO.getALL();

        // Verify
        Assertions.assertEquals(beforeRoles.size() + 2, roles.size());

        // Cleanup
        roleDAO.delete(role1);
        roleDAO.delete(role2);
    }

    @Test
    void testFindRoleByID() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        // Test
        Role retrievedRole = roleDAO.findByID(role.getId());

        // Verify
        Assertions.assertNotNull(retrievedRole);
        Assertions.assertEquals(role.getRoleName(), retrievedRole.getRoleName());
        Assertions.assertEquals(role.getId(), retrievedRole.getId());

        // Cleanup
        roleDAO.delete(role);
    }

    @Test
    void testUpdateRole() {
        // Setup
        Role role = new Role();
        role.setRoleName("OldRole");
        roleDAO.create(role);

        Role updatedRole = new Role();
        updatedRole.setId(role.getId());
        updatedRole.setRoleName("NewRole");

        // Test
        roleDAO.update(updatedRole);

        // Verify
        Role retrievedRole = roleDAO.findByID(role.getId());
        Assertions.assertNotNull(retrievedRole);
        Assertions.assertEquals(updatedRole.getRoleName(), retrievedRole.getRoleName());

        // Cleanup
        roleDAO.delete(role);
        roleDAO.delete(updatedRole);
    }

    @Test
    void testDeleteRole() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        // Test
        roleDAO.delete(role);

        // Verify
        Role deletedRole = roleDAO.findByID(role.getId());
        Assertions.assertNotNull(deletedRole);
        Assertions.assertEquals(-1L, deletedRole.getId());
        Assertions.assertEquals("emptyRoleName", deletedRole.getRoleName());
    }

    @Test
    void testSetRolePermission() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        PermissionDAO permissionDAO = new PermissionDAO();
        Permission permission = new Permission();
        permission.setPermissionName("TestPermission");
        permissionDAO.create(permission);

        // Test
        roleDAO.setRolePermission(role, permission);

        // Verify
        Role updatedRole = roleDAO.findByID(role.getId());
        Assertions.assertNotNull(updatedRole);
        List<Permission> permissions = updatedRole.getPermissions();
        Assertions.assertTrue(permissions.size() > 0);

        // Cleanup
        roleDAO.deleteRolePermission(role);
        roleDAO.delete(role);
    }

    @Test
    void testDeleteRolePermission() {
        // Setup
        Role role = new Role();
        role.setRoleName("TestRole");
        roleDAO.create(role);

        PermissionDAO permissionDAO = new PermissionDAO();
        Permission permission = new Permission();
        permission.setPermissionName("TestPermission");
        permissionDAO.create(permission);

        // Test
        roleDAO.deleteRolePermission(role);

        // Verify
        Role updatedRole = roleDAO.findByID(role.getId());
        Assertions.assertNotNull(updatedRole);
        List<Permission> permissions = updatedRole.getPermissions();
        Assertions.assertTrue(permissions.size() > 0);
    }
}
