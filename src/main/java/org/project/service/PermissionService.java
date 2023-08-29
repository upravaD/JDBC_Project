package org.project.service;

import org.project.dao.PermissionDAO;
import org.project.model.Permission;

import java.sql.Connection;
import java.util.List;

public class PermissionService implements Service<Permission> {
    private final PermissionDAO permissionDAO;

    public PermissionService(Connection connection) {
        this.permissionDAO = new PermissionDAO(connection);
    }

    @Override
    public void add(Permission permission) {
        permissionDAO.create(permission);
    }

    @Override
    public Permission read(Long id) {
        return permissionDAO.findByID(id);
    }

    @Override
    public List<Permission> readAll() {
        return permissionDAO.getALL();
    }

    @Override
    public void update(Permission permission) {
        if (isExist(permission.getId())) {
            permissionDAO.update(permission);
        } else {
            throw new IllegalArgumentException("Введен неверный Id");
        }
    }

    @Override
    public void remove(Permission permission) {
        permissionDAO.deleteRolePermission(permission);
        permissionDAO.delete(permission);
    }

    private boolean isExist(Long id) {
        return permissionDAO.findByID(id).getId() != -1L;
    }
}
