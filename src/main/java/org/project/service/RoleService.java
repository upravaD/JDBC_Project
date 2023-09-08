package org.project.service;

import org.project.dao.RoleDAO;
import org.project.model.Permission;
import org.project.model.Role;

import java.util.List;

public class RoleService implements Service<Role> {
    private RoleDAO roleDAO;

    public RoleService() {
        this.roleDAO = new RoleDAO();
    }

    @Override
    public Role add(Role role) {
        roleDAO.create(role);
        return roleDAO.findByID(role.getId());
    }

    public List<Permission> setRolePermission(Role role, List<Permission> permissions) {
        for (Permission permission : permissions) {
            roleDAO.setRolePermission(role, permission);
        }
        return permissions;
    }

    @Override
    public Role read(Long id) {
        return roleDAO.findByID(id);
    }

    @Override
    public List<Role> readAll() {
        return roleDAO.getALL();
    }

    @Override
    public boolean update(Role role) {
        return roleDAO.update(role);
    }

    @Override
    public boolean remove(Role role) {
        removePermission(role);
        return roleDAO.delete(role);
    }

    public void removePermission(Role role) {
        roleDAO.deleteRolePermission(role);
    }

    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }
}
