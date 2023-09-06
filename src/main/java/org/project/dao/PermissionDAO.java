package org.project.dao;

import org.project.model.Permission;
import org.project.model.Role;
import org.project.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO implements DAO<Permission> {
    private final Connection connection;

    public PermissionDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Permission permission) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.PERMISSION_CREATE.get())) {
            preparedStatement.setString(1, permission.getPermissionName());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            permission.setId(resultSet.getLong("id"));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Permission> getALL() {
        List<Permission> permissions = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.PERMISSION_GET_ALL.get())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Permission permission = new Permission();
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
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.PERMISSION_FIND_BY_ID.get())) {
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
    public boolean update(Permission permission) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.PERMISSION_UPDATE.get())) {
            preparedStatement.setString(1, permission.getPermissionName());
            preparedStatement.setLong(2, permission.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Permission permission) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.PERMISSION_DELETE.get())) {
            preparedStatement.setLong(1, permission.getId());
            preparedStatement.setString(2, permission.getPermissionName());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRolePermission(Permission permission) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(Queries.PERMISSION_DELETE_PERMISSION_ROLE.get())) {
            preparedStatement.setLong(1, permission.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Role> getRolePermissionByID(Permission permission) {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT role_id FROM role_permissions where permission_id = ?")) {
            preparedStatement.setLong(1, permission.getId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                roles.add(new RoleDAO(connection).findByID(resultSet.getLong("role_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
