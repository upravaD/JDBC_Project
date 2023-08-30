package org.project.servlet;

import com.google.gson.Gson;
import org.project.model.User;
import org.project.service.RoleService;
import org.project.service.UserService;
import org.project.util.PostgresConnection;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends HttpServlet {
    private Connection connection;
    private UserService userService;
    private RoleService roleService;

    @Override
    public void init() {
        connection = PostgresConnection.getConnection();
        userService = new UserService(connection);
        roleService = new RoleService(connection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String requestId = request.getParameter("id");
        String json;

        if (requestId != null) {
            long id = parseLongID(requestId);
            User user = userService.read(id);
            json = new Gson().toJson(user);
        } else {
            List<User> users = userService.readAll();
            json = new Gson().toJson(users);
        }

        sendJson(response, json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestName = request.getParameter("username");
        long requestRoleId = parseLongID(request.getParameter("role_id"));

        User user = new User();
        user.setUsername(requestName);
        user.setRole(roleService.read(requestRoleId));
        userService.add(user);

        String json = new Gson().toJson(user);
        sendJson(response, json);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        long id = parseLongID(request.getParameter("id"));
        String requestName = request.getParameter("username");
        long roleId = parseLongID(request.getParameter("role_id"));

        User user = userService.read(id);
        user.setUsername(requestName);
        user.setRole(roleService.read(roleId));
        userService.update(user);

        String json = new Gson().toJson(user);
        sendJson(response, json);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        long id = parseLongID(request.getParameter("id"));

        User user = userService.read(id);
        userService.remove(user);

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendJson(HttpServletResponse response, String json) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long parseLongID(String id) {
        return Long.parseLong(id);
    }
}