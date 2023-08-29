package org.project;

import com.google.gson.Gson;
import org.project.model.User;
import org.project.service.UserService;
import org.project.util.PostgresConnection;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        try {
//            UserService userService = new UserService(PostgresConnection.getConnection());
//            User user = userService.read(7L);
//            userService.remove(user);
//            System.out.println(userService.readAll());
//            System.out.println(userService.read(user.getId()));
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

//        try {
//            RoleService roleService = new RoleService(PostgresConnection.getConnection());
////            Role role = roleService.read(45L);
////            role.setRoleName("testRole123456");
////            roleService.update(role);
////            System.out.println(role.getId());
////            PermissionService permissionService = new PermissionService(PostgresConnection.getConnection());
////            roleService.setRolePermission(role, List.of(permissionService.read(1L), permissionService.read(2L), permissionService.read(3L)));
//            roleService.remove(roleService.read(40L));
//            System.out.println(roleService.readAll());
//            System.out.println(roleService.read(39L));
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

//        try {
//            PermissionService permissionService = new PermissionService(PostgresConnection.getConnection());
//            Permission permission = permissionService.read(25L);
//            permission.setPermissionName("testPermission2");
//            permissionService.update(permission);
//            System.out.println(permission.getId());
//
//            System.out.println(permissionService.readAll());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
            UserService userService = new UserService(PostgresConnection.getConnection());
            List<User> users = userService.readAll();
            String json = new Gson().toJson(users);
            System.out.println(json);
    }
}