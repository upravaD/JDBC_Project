package util;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer {
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    private PostgresContainer(){}

    public static PostgreSQLContainer<?> getContainer() {
        return postgres.withInitScript("dbtest.sql");
    }

    public static void main(String[] args) {
        postgres = getContainer();
        postgres.start();
        System.out.println(postgres.getJdbcUrl());
        System.out.println(postgres.getDatabaseName());
        System.out.println(postgres.getUsername());
        System.out.println(postgres.getPassword());
    }
}
