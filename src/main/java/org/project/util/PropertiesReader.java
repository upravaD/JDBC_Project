package org.project.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    protected static final String CONFIG_PATH = "application.properties";
    protected final Properties properties;

    protected PropertiesReader() {
        properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_PATH);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
