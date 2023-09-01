package servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.project.model.Role;
import org.project.model.User;
import org.project.service.RoleService;
import org.project.service.UserService;
import org.project.servlet.UserServlet;
import org.project.util.PostgresConnection;
import org.project.util.PostgresPropertiesReader;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Testcontainers
class UserServletTest {
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
    private UserServlet userServlet;
    private UserService userService;
    private RoleService roleService;

    @BeforeEach
    public void setup() {
        postgres.start();
        Connection connection = PostgresConnection.getConnection(
                postgres.getDatabaseName(),
                postgres.getUsername(),
                postgres.getPassword());
        MockitoAnnotations.openMocks(this);
        userService = mock(UserService.class);
        roleService = mock(RoleService.class);
        userServlet = new UserServlet(userService, roleService, connection);
    }

    @AfterEach
    public void cleanup() {
        postgres.stop();
    }

    @Test
    void testDoGetWithId() throws Exception {
        when(request.getParameter("id")).thenReturn("1");

        User user = new User();
        user.setId(1L);
        when(userService.read(anyLong())).thenReturn(user);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        writer.flush();

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).getWriter();
        verify(userService).read(1L);
    }

    @Test
    void testDoGetWithoutId() throws Exception {
        when(request.getParameter("id")).thenReturn(null);

        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userService.readAll()).thenReturn(users);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        writer.flush();

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).getWriter();
        verify(userService).readAll();
    }

    @Test
    void testDoPost() throws Exception {
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("role_id")).thenReturn("1");

        User user = new User();
        user.setId(1L);
        when(userService.add(any(User.class))).thenReturn(user);

        Role role = new Role();
        role.setId(1L);
        when(roleService.read(1L)).thenReturn(role);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        writer.flush();

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).getWriter();
        verify(userService).add(any(User.class));
        verify(roleService).read(1L);
    }

    @Test
    void testDoPut() throws Exception {
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("username")).thenReturn("updatedUser");
        when(request.getParameter("role_id")).thenReturn("2");

        User user = new User();
        user.setId(1L);
        when(userService.read(1L)).thenReturn(user);

        Role role = new Role();
        role.setId(2L);
        when(roleService.read(2L)).thenReturn(role);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPut(request, response);

        writer.flush();

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).getWriter();
        verify(userService).read(1L);
        verify(roleService).read(2L);
        verify(userService).update(any(User.class));
    }

    @Test
    void testDoDelete() {
        when(request.getParameter("id")).thenReturn("1");

        User user = new User();
        user.setId(1L);
        when(userService.read(1L)).thenReturn(user);

        userServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(userService).read(1L);
        verify(userService).remove(user);
    }
}