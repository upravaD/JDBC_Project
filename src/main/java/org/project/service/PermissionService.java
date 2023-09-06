package org.project.service;

import org.project.dao.PermissionDAO;
import org.project.model.Permission;

import java.sql.Connection;
import java.util.List;

public class PermissionService implements Service<Permission> {
    private PermissionDAO permissionDAO;

    public PermissionService(Connection connection) {
        this.permissionDAO = new PermissionDAO(connection);
    }

    @Override
    public Permission add(Permission permission) {
        permissionDAO.create(permission);
        permission.setRoles(permissionDAO.getRolePermissionByID(permission));
        return permissionDAO.findByID(permission.getId());
    }

    @Override
    public Permission read(Long id) {
        Permission permission = permissionDAO.findByID(id);
        permission.setRoles(permissionDAO.getRolePermissionByID(permission));
        return permission;
    }

    @Override
    public List<Permission> readAll() {
        return permissionDAO.getALL();
    }

    @Override
    public boolean update(Permission permission) {
        return permissionDAO.update(permission);
    }

    @Override
    public boolean remove(Permission permission) {
        permissionDAO.deleteRolePermission(permission);
        return permissionDAO.delete(permission);
    }

    public void setPermissionDAO(PermissionDAO permissionDAO) {
        this.permissionDAO = permissionDAO;
    }
}
