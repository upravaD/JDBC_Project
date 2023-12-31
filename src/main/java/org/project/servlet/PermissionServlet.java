package org.project.servlet;

import com.google.gson.Gson;
import org.project.model.Permission;
import org.project.service.PermissionService;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PermissionServlet", value = "/permissions")
public class PermissionServlet extends HttpServlet {
    private final PermissionService permissionService;

    public PermissionServlet(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestName = request.getParameter("permission_name");

        Permission permission = new Permission();
        permission.setPermissionName(requestName);
        permissionService.add(permission);
        String json = new Gson().toJson(permission);

        sendJson(response, json);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        long requestId = parseLongID(request.getParameter("id"));
        String requestName = request.getParameter("permission_name");

        Permission permission = permissionService.read(requestId);
        permission.setPermissionName(requestName);
        permissionService.update(permission);

        String json = new Gson().toJson(permission);
        sendJson(response, json);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        long requestId = parseLongID(request.getParameter("id"));

        Permission permission = permissionService.read(requestId);
        permissionService.remove(permission);

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
