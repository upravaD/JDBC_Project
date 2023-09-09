package org.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private static String url;
    private static String name;
    private static String pass;

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
            try {
                return DriverManager.getConnection(url, name, pass);
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    public static void setParameters(String url, String name, String pass) {
        PostgresConnection.url = url;
        PostgresConnection.name = name;
        PostgresConnection.pass = pass;
    }

    private static void setDatabaseDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
