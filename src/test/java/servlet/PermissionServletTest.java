package servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.project.model.Permission;
import org.project.service.PermissionService;
import org.project.servlet.PermissionServlet;
import org.project.util.PostgresConnection;
import org.project.util.PostgresPropertiesReader;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
@Testcontainers
class PermissionServletTest {
    private static final PostgresPropertiesReader propertiesReader = new PostgresPropertiesReader();
    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName(propertiesReader.getUrl())
            .withUsername(propertiesReader.getUser())
            .withPassword(propertiesReader.getPassword());
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private PermissionServlet permissionServlet;
    private PermissionService permissionService;

    @BeforeEach
    public void setup() {
        postgres.start();
        Connection connection = PostgresConnection.getConnection(
                postgres.getDatabaseName(),
                postgres.getUsername(),
                postgres.getPassword());
        MockitoAnnotations.openMocks(this);
        permissionService = mock(PermissionService.class);
        permissionServlet = new PermissionServlet(permissionService, connection);
    }

    @AfterEach
    public void cleanup() {
        postgres.stop();
    }

    @Test
    void testDoGetWithId() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setPermissionName("TestPermission");
        when(permissionService.read(1L)).thenReturn(permission);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        permissionServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        writer.flush();

        String jsonResult = stringWriter.toString();
        Permission parsedPermission = new ObjectMapper().readValue(jsonResult, Permission.class);
        Assertions.assertNotNull(parsedPermission);
        Assertions.assertEquals(permission.getId(), parsedPermission.getId());
        Assertions.assertEquals(permission.getPermissionName(), parsedPermission.getPermissionName());
    }

    @Test
    void testDoGetWithoutId() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        List<Permission> permissions = new ArrayList<>();

        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setPermissionName("TestPermission1");
        permissions.add(permission1);

        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setPermissionName("TestPermission2");
        permissions.add(permission2);

        when(permissionService.readAll()).thenReturn(permissions);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        permissionServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        writer.flush();

        String jsonResult = stringWriter.toString();
        Permission[] parsedPermissions = new ObjectMapper().readValue(jsonResult, Permission[].class);
        Assertions.assertNotNull(parsedPermissions);
        Assertions.assertEquals(permissions.size(), parsedPermissions.length);
        Assertions.assertEquals(permissions.get(0).getId(), parsedPermissions[0].getId());
        Assertions.assertEquals(permissions.get(0).getPermissionName(), parsedPermissions[0].getPermissionName());
        Assertions.assertEquals(permissions.get(1).getId(), parsedPermissions[1].getId());
        Assertions.assertEquals(permissions.get(1).getPermissionName(), parsedPermissions[1].getPermissionName());
    }

    @Test
    void testDoPost() throws IOException {
        when(request.getParameter("permission_name")).thenReturn("TestPermission");

        Permission createdPermission = new Permission();
        createdPermission.setId(1L);
        createdPermission.setPermissionName("TestPermission");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        permissionServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        writer.flush();

        String jsonResult = stringWriter.toString();
        Permission parsedPermission = new ObjectMapper().readValue(jsonResult, Permission.class);
        Assertions.assertNotNull(parsedPermission);
        Assertions.assertEquals(createdPermission.getPermissionName(), parsedPermission.getPermissionName());
    }

    @Test
    void testDoPut() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("permission_name")).thenReturn("UpdatedPermission");

        Permission existingPermission = new Permission();
        existingPermission.setId(1L);
        existingPermission.setPermissionName("TestPermission");
        when(permissionService.read(1L)).thenReturn(existingPermission);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        permissionServlet.doPut(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        writer.flush();

        String jsonResult = stringWriter.toString();
        Permission parsedPermission = new ObjectMapper().readValue(jsonResult, Permission.class);
        Assertions.assertNotNull(parsedPermission);
        Assertions.assertEquals(existingPermission.getId(), parsedPermission.getId());
        Assertions.assertEquals("UpdatedPermission", parsedPermission.getPermissionName());
    }

    @Test
    void testDoDelete() {
        when(request.getParameter("id")).thenReturn("1");

        Permission existingPermission = new Permission();
        existingPermission.setId(1L);
        existingPermission.setPermissionName("TestPermission");
        when(permissionService.read(1L)).thenReturn(existingPermission);

        permissionServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(permissionService).remove(existingPermission);
    }
}
