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
            throw new RuntimeException();
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
