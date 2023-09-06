package service;

import org.junit.Before;
import org.junit.Test;
import org.project.dao.PermissionDAO;
import org.project.model.Permission;
import org.project.service.PermissionService;
import org.project.util.PostgresConnection;
import org.project.util.PostgresPropertiesReader;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
public class PermissionServiceTest {
    private static final PostgresPropertiesReader propertiesReader = new PostgresPropertiesReader();
    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName(propertiesReader.getDataBaseName())
            .withUsername(propertiesReader.getUser())
            .withPassword(propertiesReader.getPassword());

    private PermissionService permissionService;
    private PermissionDAO mockPermissionDAO;

    @Before
    public void setUp() {
        mockPermissionDAO = mock(PermissionDAO.class);
        Connection connection = PostgresConnection.getConnection(
                postgres.getDatabaseName(),
                postgres.getUsername(),
                postgres.getPassword());
        permissionService = new PermissionService(connection);
        permissionService.setPermissionDAO(mockPermissionDAO);
    }

    @Test
    public void testAddPermission() {
        Permission permissionToAdd = new Permission();
        permissionToAdd.setId(1L);
        permissionToAdd.setPermissionName("TestPermission");

        when(mockPermissionDAO.create(permissionToAdd)).thenReturn(true);
        when(mockPermissionDAO.findByID(anyLong())).thenReturn(permissionToAdd);

        Permission addedPermission = permissionService.add(permissionToAdd);

        assertEquals("TestPermission", addedPermission.getPermissionName());
    }

    @Test
    public void testReadPermission() {
        Permission permissionToRead = new Permission();
        permissionToRead.setId(1L);
        permissionToRead.setPermissionName("TestPermission");

        when(mockPermissionDAO.findByID(1L)).thenReturn(permissionToRead);

        Permission readPermission = permissionService.read(1L);

        assertEquals("TestPermission", readPermission.getPermissionName());
    }

    @Test
    public void testReadAllPermissions() {
        List<Permission> permissions = new ArrayList<>();
        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setPermissionName("Permission1");
        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setPermissionName("Permission2");
        permissions.add(permission1);
        permissions.add(permission2);

        when(mockPermissionDAO.getALL()).thenReturn(permissions);

        List<Permission> allPermissions = permissionService.readAll();

        assertEquals(2, allPermissions.size());
        assertEquals("Permission1", allPermissions.get(0).getPermissionName());
        assertEquals("Permission2", allPermissions.get(1).getPermissionName());
    }

    @Test
    public void testUpdatePermission() {
        Permission permissionToUpdate = new Permission();
        permissionToUpdate.setId(1L);
        permissionToUpdate.setPermissionName("TestPermission");

        when(mockPermissionDAO.update(permissionToUpdate)).thenReturn(true);

        boolean result = permissionService.update(permissionToUpdate);

        assertTrue(result);
    }

    @Test
    public void testRemovePermission() {
        Permission permissionToRemove = new Permission();
        permissionToRemove.setId(1L);

        when(mockPermissionDAO.deleteRolePermission(permissionToRemove)).thenReturn(true);
        when(mockPermissionDAO.delete(permissionToRemove)).thenReturn(true);

        boolean result = permissionService.remove(permissionToRemove);

        assertTrue(result);
    }
}
