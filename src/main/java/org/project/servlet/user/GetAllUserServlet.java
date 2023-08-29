package org.project.servlet.user;

import com.google.gson.Gson;
import org.project.model.User;
import org.project.service.UserService;
import org.project.util.PostgresConnection;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "getAllUsers", value = "/users")
public class GetAllUserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = new UserService(PostgresConnection.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
            List<User> users = userService.readAll();
            String json = new Gson().toJson(users);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
