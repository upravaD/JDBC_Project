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

    private static final String ROLE_GET_ALL = "SELECT * FROM roles WHERE id = ?";
    private static final String SELECT_ROLE_BY_ID = "SELECT r.id AS role_id, r.role_name, p.id AS permission_id, p.permission_name FROM roles r LEFT JOIN role_permissions rp ON r.id = rp.role_id LEFT JOIN permissions p ON rp.permission_id = p.id WHERE r.id = ?";

    @Override
    public void create(Role value) {
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(Queries.ROLE_CREATE.get())) {

            preparedStatement.setString(1, value.getRoleName());
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
            Role role;

            while (resultSet.next()) {
                role = new Role();
                role.setId(resultSet.getLong("id"));
                role.setRoleName(resultSet.getString("role_name"));
                List<Permission> permissions = new ArrayList<>();

                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public Role findByID(int id) {
        Role role = new Role();
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROLE_BY_ID)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean roleFound = false;
                while (resultSet.next()) {
                    if (!roleFound) {
                        role.setId(resultSet.getLong("role_id"));
                        role.setRoleName(resultSet.getString("role_name"));
                        roleFound = true;
                    }
                    PermissionDAO permissionDAO = new PermissionDAO();
                    Permission permission = permissionDAO.findByID(resultSet.getInt("permission_id"));
                    role.setPermissions(new ArrayList<>());
                    role.getPermissions().add(permission);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    @Override
    public void update(Role value) {

    }

    @Override
    public void delete(Role value) {

    }
}
