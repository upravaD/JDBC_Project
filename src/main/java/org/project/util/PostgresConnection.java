package org.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private PostgresConnection(){}

    public static Connection getConnection() throws SQLException {

        PropertiesReader propertiesReader = new PropertiesReader();
        return DriverManager.getConnection(
                propertiesReader.properties.getProperty("URL"),
                propertiesReader.properties.getProperty("USER"),
                propertiesReader.properties.getProperty("PASSWORD")
        );
    }
}
