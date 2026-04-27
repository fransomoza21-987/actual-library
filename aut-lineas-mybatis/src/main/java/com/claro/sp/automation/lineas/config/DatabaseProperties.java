package com.claro.sp.automation.lineas.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseProperties {
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    private int maximumPoolSize = 5;
    private long connectionTimeoutMs = 30000;
}
