package org.project.util;

public class PostgresPropertiesReader extends PropertiesReader {

    private final String url;
    private final String dataBaseName;
    private final String user;
    private final String password;

    public PostgresPropertiesReader() {
        this.url = properties.getProperty("URL");
        this.dataBaseName = properties.getProperty("DATABASE_NAME");
        this.user = properties.getProperty("USER");
        this.password = properties.getProperty("PASSWORD");
    }

    public String getUrl() {
        return url;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
