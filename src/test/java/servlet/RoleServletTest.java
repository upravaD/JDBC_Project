package servlet;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.project.model.Permission;
import org.project.model.Role;
import org.project.service.PermissionService;
import org.project.service.RoleService;
import org.project.servlet.RoleServlet;
import org.project.util.PostgresConnection;
import org.project.util.PostgresPropertiesReader;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@Testcontainers
class RoleServletTest {
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
    private RoleServlet roleServlet;
    private RoleService roleService;

    @BeforeEach
    public void setup() {
        postgres.start();
        Connection connection = PostgresConnection.getConnection(
                postgres.getDatabaseName(),
                postgres.getUsername(),
                postgres.getPassword());
        MockitoAnnotations.openMocks(this);
        roleService = mock(RoleService.class);
        PermissionService permissionService = mock(PermissionService.class);
        roleServlet = new RoleServlet(roleService, permissionService, connection);
    }

    @AfterEach
    public void cleanup() {
        postgres.stop();
    }

    @Test
    void testDoGetWithIdParameter() throws Exception {
        when(request.getParameter("id")).thenReturn("1");

        Role role = new Role();
        role.setId(1L);
        role.setRoleName("TestRole");
        when(roleService.read(1L)).thenReturn(role);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        roleServlet.doGet(request, response);

        writer.flush();

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String expectedJson = "{\"id\":1,\"roleName\":\"TestRole\"}";
        String actualJson = stringWriter.toString().trim();
        Assertions.assertEquals(expectedJson, actualJson);
    }

    @Test
    void testDoGetWithoutIdParameter() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setId(1L);
        role1.setRoleName("Role1");
        roles.add(role1);

        Role role2 = new Role();
        role2.setId(2L);
        role2.setRoleName("Role2");
        roles.add(role2);

        when(roleService.readAll()).thenReturn(roles);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        roleServlet.doGet(request, response);

        writer.flush();

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String expectedJson = "[{\"id\":1,\"roleName\":\"Role1\"},"
                + "{\"id\":2,\"roleName\":\"Role2\"}]";
        String actualJson = stringWriter.toString().trim();
        Assertions.assertEquals(expectedJson, actualJson);
    }

    @Test
    void testDoPost() throws Exception {
        when(request.getParameter("role_name")).thenReturn("TestRole");
        when(request.getParameterValues("permissionIds")).thenReturn(new String[]{"1", "2"});

        Role role = new Role();
        role.setRoleName("TestRole");
        role.setId(1L);
        when(roleService.add(any(Role.class))).thenReturn(role);

        Permission permission1 = new Permission();
        permission1.setId(1L);
        Permission permission2 = new Permission();
        permission2.setId(2L);
        role.setPermissions(Arrays.asList(permission1, permission2));
        when(roleService.setRolePermission(any(Role.class), anyList())).thenReturn(Arrays.asList(permission1, permission2));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        roleServlet.doPost(request, response);

        writer.flush();

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String expectedJson = "{\"id\":1,\"roleName\":\"TestRole\",\"permissions\":[{\"id\":1},{\"id\":2}]}";
        String actualJson = new Gson().toJson(role);
        Assertions.assertEquals(expectedJson, actualJson);
    }

    @Test
    void testDoPut() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("role_name")).thenReturn("UpdatedRole");
        when(request.getParameterValues("permissionIds")).thenReturn(new String[]{"1", "2"});

        Role role = new Role();
        role.setRoleName("UpdatedRole");
        role.setId(1L);
        when(roleService.read(anyLong())).thenReturn(role);

        Permission permission1 = new Permission();
        permission1.setId(1L);
        Permission permission2 = new Permission();
        permission2.setId(2L);
        role.setPermissions(Arrays.asList(permission1, permission2));
        when(roleService.setRolePermission(any(Role.class), anyList())).thenReturn(Arrays.asList(permission1, permission2));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        roleServlet.doPut(request, response);

        writer.flush();

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        String expectedJson = "{\"id\":1,\"roleName\":\"UpdatedRole\",\"permissions\":[{\"id\":1},{\"id\":2}]}";
        String actualJson = stringWriter.toString().trim();
        Assertions.assertEquals(expectedJson, actualJson);
    }

    @Test
    void testDoDelete() throws Exception {
        when(request.getParameter("id")).thenReturn("1");

        Role role = new Role();
        role.setId(1L);
        when(roleService.read(anyLong())).thenReturn(role);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        roleServlet.doDelete(request, response);

        writer.flush();

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
