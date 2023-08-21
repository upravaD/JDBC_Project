package org.project.dao;

import org.project.model.Role;
import org.project.util.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO {

    private static final String SELECT_ROLE_BY_ID = "SELECT * FROM roles WHERE id = ?";

    public Role findById(int id) {
        Role role = null;
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROLE_BY_ID)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    role = new Role();
                    role.setId(resultSet.getLong("id"));
                    role.setRoleName(resultSet.getString("role_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
}
