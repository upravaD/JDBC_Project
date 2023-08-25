package org.project;

import org.project.dao.PermissionDAO;
import org.project.dao.RoleDAO;
import org.project.dao.UserDAO;
import org.project.model.Permission;
import org.project.model.Role;

public class Main {
    public static void main(String[] args) {
//        //TEST permission
//        PermissionDAO permissionDAO = new PermissionDAO();
//        Permission permission = new Permission();
//
//        permission.setId(14L);
//        permission.setPermissionName("е");
//        //permissionDAO.create(permission);
//
//        //System.out.println(permissionDAO.findByID(8));
//        permissionDAO.delete(permission);
//        System.out.println(permissionDAO.getALL());
//        //permissionDAO.update(permission);
//        System.out.println(permissionDAO.getALL());

        RoleDAO roleDAO = new RoleDAO();
//        Role role = new Role();
//        role.setRoleName("testRoleDAO");
//        roleDAO.create(role);
        System.out.println(roleDAO.getALL());
    }
}