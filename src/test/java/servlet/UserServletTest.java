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
import util.PostgresContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Testcontainers
class UserServletTest {
    @Container
    private static PostgreSQLContainer<?> postgres = PostgresContainer.getContainer();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    private UserServlet userServlet;

    @BeforeEach
    public void setup() {
        postgres.start();
        MockitoAnnotations.openMocks(this);
        userServlet = new UserServlet(userService, roleService);
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