package org.project.service;

import org.project.dao.PermissionDAO;
import org.project.model.Permission;

import java.util.List;

public class PermissionService implements Service<Permission> {

    private final PermissionDAO permissionDAO = new PermissionDAO();
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
        permissionDAO.update(permission);
    }

    @Override
    public void remove(Permission permission) {
        permissionDAO.delete(permission);
    }
}
