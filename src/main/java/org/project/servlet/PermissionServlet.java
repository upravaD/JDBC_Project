package org.project.servlet;

import com.google.gson.Gson;
import org.project.model.Permission;
import org.project.service.PermissionService;
import org.project.util.PostgresConnection;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PermissionServlet", value = "/permissions")
public class PermissionServlet extends HttpServlet {
    private Connection connection;
    private PermissionService permissionService;

    @Override
    public void init() {
        connection = PostgresConnection.getConnection();
        permissionService = new PermissionService(connection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String requestId = request.getParameter("id");
        String json;

        if (requestId != null) {
            long id = parseLongID(requestId);
            Permission permission = permissionService.read(id);
            json = new Gson().toJson(permission);
        } else {
            List<Permission> permissions = permissionService.readAll();
            json = new Gson().toJson(permissions);
        }
        sendJson(response, json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestName = request.getParameter("permission_name");

        Permission permission = new Permission();
        permission.setPermissionName(requestName);
        permissionService.add(permission);
        String json = new Gson().toJson(permission);

        sendJson(response, json);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        long requestId = parseLongID(request.getParameter("id"));
        String requestName = request.getParameter("permission_name");

        Permission permission = permissionService.read(requestId);
        permission.setPermissionName(requestName);
        permissionService.update(permission);

        String json = new Gson().toJson(permission);
        sendJson(response, json);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        long requestId = parseLongID(request.getParameter("id"));

        Permission permission = permissionService.read(requestId);
        permissionService.remove(permission);

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
