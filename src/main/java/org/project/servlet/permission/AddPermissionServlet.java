package org.project.servlet.permission;

import com.google.gson.Gson;
import org.project.model.Permission;
import org.project.service.PermissionService;
import org.project.util.PostgresConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/permissions/add")
public class AddPermissionServlet extends HttpServlet {
    private PermissionService permissionService;

    @Override
    public void init() {
        permissionService = new PermissionService(PostgresConnection.getConnection());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("permission_name");
        Permission permission = new Permission();
        permission.setPermissionName(name);
        permissionService.add(permission);

        try {
            String json = new Gson().toJson(permission);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
