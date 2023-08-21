package org.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private PostgresConnection(){}

    public static Connection getConnection() throws SQLException {
        PostgresPropertiesReader propertiesReader = new PostgresPropertiesReader();
        return DriverManager.getConnection(
                propertiesReader.getUrl(),
                propertiesReader.getUser(),
                propertiesReader.getPassword()
        );
    }
}
