package org.project.servlet;

import com.google.gson.Gson;
import org.project.model.User;
import org.project.service.RoleService;
import org.project.service.UserService;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends HttpServlet {
    private final UserService userService;
    private final RoleService roleService;

    public UserServlet(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
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
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
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
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        long id = parseLongID(request.getParameter("id"));

        User user = userService.read(id);
        userService.remove(user);

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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