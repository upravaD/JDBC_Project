package org.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private PostgresConnection(){}
    public static Connection getConnection() {
        PostgresPropertiesReader propertiesReader = new PostgresPropertiesReader();
        setDatabaseDriver();
        try {
            return DriverManager.getConnection(
                    propertiesReader.getUrl() + propertiesReader.getDataBaseName(),
                    propertiesReader.getUser(),
                    propertiesReader.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(String dbName, String username, String password) {
        PostgresPropertiesReader propertiesReader = new PostgresPropertiesReader();
        setDatabaseDriver();
        try {
            return DriverManager.getConnection(
                    (propertiesReader.getUrl() + dbName),
                    username,
                    password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setDatabaseDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
