package util;

import org.project.util.PostgresPropertiesReader;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    private PostgresContainer(){}

    public static PostgreSQLContainer<?> getContainer() {
        PostgresPropertiesReader propertiesReader = new PostgresPropertiesReader();
        return postgres
                .withDatabaseName(propertiesReader.getDataBaseName())
                .withUsername(propertiesReader.getUser())
                .withPassword(propertiesReader.getPassword());
    }
}
