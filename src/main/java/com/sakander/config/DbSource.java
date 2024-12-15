package com.sakander.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
@Component
@Slf4j
public class DbSource {
    private static HikariDataSource dataSource;
    @Autowired
    private DbSourceProperties dataSourceProperties;
    @PostConstruct
    public void init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPassword());
        config.setDriverClassName(dataSourceProperties.getDriverClassName());
        config.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
        config.setAutoCommit(dataSourceProperties.isAutoCommit());
        config.setIdleTimeout(dataSourceProperties.getIdleTimeout());
        config.setMaxLifetime(dataSourceProperties.getMaxLifetime());
        config.setConnectionTimeout(dataSourceProperties.getConnectionTimeout());
        dataSource = new HikariDataSource(config);
    }
    @Bean
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            log.warn("No database connection available");
            throw new IllegalStateException("DataSource is not initialized.");
        }
        return dataSource.getConnection();
    }
}
