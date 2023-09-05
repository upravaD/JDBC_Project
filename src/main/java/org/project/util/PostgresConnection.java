package org.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private static Connection connection;
    private PostgresConnection(){}
    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            PostgresPropertiesReader propertiesReader = new PostgresPropertiesReader();
            connection = DriverManager.getConnection(
                    propertiesReader.getUrl(),
                    propertiesReader.getUser(),
                    propertiesReader.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getConnection(String url, String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(("jdbc:postgresql://localhost:5432/" + url), username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
