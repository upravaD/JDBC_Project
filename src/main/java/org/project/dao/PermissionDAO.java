package org.project.dao;

import org.project.model.Permission;
import org.project.util.PostgresConnection;
import org.project.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO implements DAO<Permission> {

    @Override
    public void create(Permission permission) {
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.PERMISSION_CREATE.get())) {

            preparedStatement.setString(1, permission.getPermissionName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Permission> getALL() {
        List<Permission> permissions = new ArrayList<>();

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.PERMISSION_GET_ALL.get())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Permission permission;

            while (resultSet.next()) {
                permission = new Permission();
                permission.setId(resultSet.getLong("id"));
                permission.setPermissionName(resultSet.getString("permission_name"));
                permissions.add(permission);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permissions;
    }

    @Override
    public Permission findByID(Long id) {
        Permission permission = null;
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.PERMISSION_FIND_BY_ID.get())) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                permission = new Permission();
                permission.setId(resultSet.getLong("id"));
                permission.setPermissionName(resultSet.getString("permission_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (permission == null) {
            permission = new Permission(); // Создаем пустой объект, чтобы избежать возврата null
            permission.setId(-1L);
            permission.setPermissionName("emptyPermissionName");
        }

        return permission;
    }

    @Override
    public void update(Permission permission) {
        if (isExist(permission.getId())) {
            try (Connection connection = PostgresConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(Queries.PERMISSION_UPDATE.get())) {

                preparedStatement.setString(1, permission.getPermissionName());
                preparedStatement.setLong(2, permission.getId());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Permission permission) {
        if (isExist(permission.getId())) {
            deleteRolePermission(permission);

            try (Connection connection = PostgresConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(Queries.PERMISSION_DELETE.get())) {

                preparedStatement.setLong(1, permission.getId());
                preparedStatement.setString(2, permission.getPermissionName());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRolePermission(Permission permission) {
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.PERMISSION_DELETE_PERMISSION_ROLE.get())) {

            preparedStatement.setLong(1, permission.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isExist(Long id) {
        return findByID(id).getId() != -1L;
    }
}