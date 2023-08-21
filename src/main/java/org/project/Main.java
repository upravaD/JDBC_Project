package org.project;

import org.project.util.PostgresConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection connection  = PostgresConnection.getConnection();

        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM roles");

        while (results.next()) {
            Integer id = results.getInt(1);
            String name = results.getString(2);
            System.out.println(id + " " + name);
        }
        connection.close();
    }
}