package org.project.dao;

import org.project.model.Permission;
import org.project.model.Role;
import org.project.util.PostgresConnection;
import org.project.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO implements DAO<Role> {

    @Override
    public void create(Role role) {
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.ROLE_CREATE.get())) {

            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> getALL() {
        List<Role> roles = new ArrayList<>();

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.ROLE_GET_ALL.get())) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roles.add(findByID(resultSet.getLong("id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public Role findByID(Long id) {
        Role role = new Role();
        List<Permission> tempPermissions = new ArrayList<>();

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.ROLE_FIND_BY_ID.get())) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean roleFound = false;
            while (resultSet.next()) {
                if (!roleFound) {
                    role.setId(resultSet.getLong("role_id"));
                    role.setRoleName(resultSet.getString("role_name"));
                    roleFound = true;
                }
                PermissionDAO permissionDAO = new PermissionDAO();
                Permission permission = permissionDAO.findByID(resultSet.getLong("permission_id"));
                tempPermissions.add(permission);
            }
            role.setPermissions(tempPermissions);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (role.getId() == null) {
            role.setId(-1L);
            role.setRoleName("emptyRoleName");
        }
        return role;
    }

    @Override
    public void update(Role role) {
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.ROLE_UPDATE.get())) {

            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.setLong(2, role.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Role role) {
        deleteRolePermission(role);

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.ROLE_DELETE.get())) {

            preparedStatement.setLong(1, role.getId());
            preparedStatement.setString(2, role.getRoleName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRolePermission(Role role, Permission permission) {
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.ROLE_SET_PERMISSIONS.get())) {

            preparedStatement.setLong(1, role.getId());
            preparedStatement.setLong(2, permission.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRolePermission(Role role) {
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.ROLE_DELETE_ROLE_PERMISSION.get())) {

            preparedStatement.setLong(1, role.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
