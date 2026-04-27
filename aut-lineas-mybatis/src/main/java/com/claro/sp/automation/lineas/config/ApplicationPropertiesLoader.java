package com.claro.sp.automation.lineas.config;

import com.claro.sp.automation.lineas.exception.LineasException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesLoader {
    public Properties load() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                throw new LineasException("No se encontro application.properties");
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new LineasException("No se pudo leer application.properties", e);
        }
    }

    public DatabaseProperties databaseProperties(Properties properties, String prefix) {
        DatabaseProperties databaseProperties = new DatabaseProperties();
        databaseProperties.setJdbcUrl(required(properties, prefix + ".jdbcUrl"));
        databaseProperties.setUsername(required(properties, prefix + ".username"));
        databaseProperties.setPassword(required(properties, prefix + ".password"));
        databaseProperties.setDriverClassName(required(properties, prefix + ".driverClassName"));
        databaseProperties.setMaximumPoolSize(intValue(properties, prefix + ".maximumPoolSize", 5));
        databaseProperties.setConnectionTimeoutMs(longValue(properties, prefix + ".connectionTimeoutMs", 30000));
        return databaseProperties;
    }

    private String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new LineasException("Falta configurar la propiedad " + key);
        }
        return value;
    }

    private int intValue(Properties properties, String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value == null || value.isBlank() ? defaultValue : Integer.parseInt(value);
    }

    private long longValue(Properties properties, String key, long defaultValue) {
        String value = properties.getProperty(key);
        return value == null || value.isBlank() ? defaultValue : Long.parseLong(value);
    }
}
