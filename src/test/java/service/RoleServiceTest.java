package service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.project.dao.RoleDAO;
import org.project.model.Permission;
import org.project.model.Role;
import org.project.service.RoleService;
import org.project.util.PostgresConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import util.PostgresContainer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Testcontainers
public class RoleServiceTest {
    @Container
    private static PostgreSQLContainer<?> postgres = PostgresContainer.getContainer();
    @Mock
    private RoleDAO mockRoleDAO;
    private RoleService roleService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Connection mockConnection = PostgresConnection.getConnection(
                postgres.getDatabaseName(),
                postgres.getUsername(),
                postgres.getPassword());
        roleService = new RoleService(mockConnection);
        roleService.setRoleDAO(mockRoleDAO);
    }

    @Test
    public void testAddRole() {
        Role roleToAdd = new Role();
        roleToAdd.setId(1L);
        roleToAdd.setRoleName("TestRole");

        when(mockRoleDAO.create(roleToAdd)).thenReturn(true);
        when(mockRoleDAO.findByID(anyLong())).thenReturn(roleToAdd);

        Role addedRole = roleService.add(roleToAdd);

        assertEquals("TestRole", addedRole.getRoleName());
    }

    @Test
    public void testSetRolePermission() {
        Role role = new Role();
        role.setId(1L);
        Permission permission1 = new Permission();
        permission1.setId(1L);
        Permission permission2 = new Permission();
        permission2.setId(2L);
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission1);
        permissions.add(permission2);

        when(mockRoleDAO.setRolePermission(role, permissions.get(0))).thenReturn(true);
        when(mockRoleDAO.setRolePermission(role, permissions.get(1))).thenReturn(true);

        List<Permission> resultPermissions = roleService.setRolePermission(role, permissions);

        assertEquals(2, resultPermissions.size());
    }

    @Test
    public void testReadRole() {
        Role roleToRead = new Role();
        roleToRead.setId(1L);
        roleToRead.setRoleName("TestRole");

        when(mockRoleDAO.findByID(1L)).thenReturn(roleToRead);

        Role readRole = roleService.read(1L);

        assertEquals("TestRole", readRole.getRoleName());
    }

    @Test
    public void testReadAllRoles() {
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setId(1L);
        role1.setRoleName("Role1");
        Role role2 = new Role();
        role2.setId(2L);
        role2.setRoleName("Role2");
        roles.add(role1);
        roles.add(role2);

        when(mockRoleDAO.getALL()).thenReturn(roles);

        List<Role> allRoles = roleService.readAll();

        assertEquals(2, allRoles.size());
        assertEquals("Role1", allRoles.get(0).getRoleName());
        assertEquals("Role2", allRoles.get(1).getRoleName());
    }

    @Test
    public void testUpdateRole() {
        Role roleToUpdate = new Role();
        roleToUpdate.setId(1L);
        roleToUpdate.setRoleName("TestRole");

        when(mockRoleDAO.update(roleToUpdate)).thenReturn(true);

        boolean result = roleService.update(roleToUpdate);

        assertTrue(result);
    }

    @Test
    public void testRemoveRole() {
        Role roleToRemove = new Role();
        roleToRemove.setId(1L);

        when(mockRoleDAO.deleteRolePermission(roleToRemove)).thenReturn(true);
        when(mockRoleDAO.delete(roleToRemove)).thenReturn(true);

        boolean result = roleService.remove(roleToRemove);

        assertTrue(result);
    }
}