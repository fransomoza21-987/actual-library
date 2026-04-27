package com.claro.sp.automation.lineas.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariDataSourceFactory {
    public HikariDataSource create(DatabaseProperties properties, DataSourceName dataSourceName) {
        HikariConfig config = new HikariConfig();
        config.setPoolName("lineas-" + dataSourceName.name().toLowerCase());
        config.setJdbcUrl(properties.getJdbcUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setDriverClassName(properties.getDriverClassName());
        config.setMaximumPoolSize(properties.getMaximumPoolSize());
        config.setConnectionTimeout(properties.getConnectionTimeoutMs());
        return new HikariDataSource(config);
    }
}
