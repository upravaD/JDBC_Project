package org.project.servlet;

import com.google.gson.Gson;
import org.project.model.Permission;
import org.project.model.Role;
import org.project.service.PermissionService;
import org.project.service.RoleService;
import org.project.util.PostgresConnection;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RoleServlet", value = "/roles")
public class RoleServlet extends HttpServlet {
    private Connection connection = PostgresConnection.getConnection();
    private RoleService roleService;
    private PermissionService permissionService;

    public RoleServlet() {
    }

    public RoleServlet(RoleService roleService, PermissionService permissionService, Connection connection) {
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.connection = connection;
    }

    @Override
    public void init() {
        roleService = new RoleService(connection);
        permissionService = new PermissionService(connection);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String idParam = request.getParameter("id");
        String json;

        if (idParam != null) {
            long id = parseLongID(idParam);
            Role role = roleService.read(id);
            json = new Gson().toJson(role);
        } else {
            List<Role> roles = roleService.readAll();
            json = new Gson().toJson(roles);
        }
        sendJson(response, json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestName = request.getParameter("role_name");
        String[] permissionIds = request.getParameterValues("permissionIds");

        Role role = new Role();
        role.setRoleName(requestName);
        roleService.add(role);

        setPermission(role, permissionIds);

        String json = new Gson().toJson(role);
        sendJson(response, json);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        long requestId = parseLongID(request.getParameter("id"));
        String requestName = request.getParameter("role_name");
        String[] permissionIds = request.getParameterValues("permissionIds");

        Role role = roleService.read(requestId);
        role.setRoleName(requestName);
        roleService.update(role);

        setPermission(role, permissionIds);

        String json = new Gson().toJson(role);
        sendJson(response, json);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        long requestId = parseLongID(req.getParameter("id"));

        Role role = roleService.read(requestId);
        roleService.remove(role);

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
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

    private void setPermission(Role role, String[] permissionIds) {
        if (permissionIds != null) {
            roleService.removePermission(role);

            List<Permission> permissions = new ArrayList<>();
            for (String permissionId : permissionIds) {
                long id = parseLongID(permissionId);
                Permission permission = permissionService.read(id);
                permissions.add(permission);
            }
            roleService.setRolePermission(role, permissions);
        }
    }
}
