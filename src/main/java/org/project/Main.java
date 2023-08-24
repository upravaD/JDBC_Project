package org.project;

import org.project.dao.PermissionDAO;
import org.project.dao.UserDAO;
import org.project.model.Permission;

public class Main {
    public static void main(String[] args) {
//        UserDAO userDAO = new UserDAO();
//        System.out.println(userDAO.getALL());
        PermissionDAO permissionDAO = new PermissionDAO();
        Permission permission = new Permission();

        permission.setId(14L);
        permission.setPermissionName("е");
        //permissionDAO.create(permission);

        //System.out.println(permissionDAO.findByID(8));
        permissionDAO.delete(permission);
        System.out.println(permissionDAO.getALL());
        //permissionDAO.update(permission);
        System.out.println(permissionDAO.getALL());

    }
}