package org.project.service;

import org.project.dao.RoleDAO;
import org.project.model.Permission;
import org.project.model.Role;

import java.util.List;

public class RoleService implements Service<Role> {
    private final RoleDAO roleDAO;

    public RoleService() {
        this.roleDAO = new RoleDAO();
    }

    @Override
    public void add(Role role) {
        roleDAO.create(role);
    }

    public void setRolePermission(Role role, List<Permission> permissions) {
        for (Permission permission : permissions) {
            roleDAO.setRolePermission(role, permission);
        }
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
    public void update(Role role) {
        roleDAO.update(role);
    }

    @Override
    public void remove(Role role) {
        roleDAO.update(role);
    }
}
