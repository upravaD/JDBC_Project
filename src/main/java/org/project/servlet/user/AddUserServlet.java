package org.project.servlet.user;

import com.google.gson.Gson;
import org.project.model.User;
import org.project.service.RoleService;
import org.project.service.UserService;
import org.project.util.PostgresConnection;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "addUser", value = "/users/add")
public class AddUserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService(PostgresConnection.getConnection());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("username");
        Long roleId = Long.valueOf(request.getParameter("role_id"));
        User user = new User();
        user.setUsername(name);
        user.setRole(new RoleService(PostgresConnection.getConnection()).read(roleId));
        userService.add(user);

        String json = new Gson().toJson(user);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
