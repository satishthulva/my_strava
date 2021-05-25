package com.satish.dao.impl;

import com.satish.config.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Hikari Connection Pool for MySQL DB
 *
 * @author satish.thulva.
 **/
public class DBDataSource {
    private static DBDataSource INSTANCE;
    private final DataSource dataSource;

    public static DBDataSource getInstance() {
        return INSTANCE;
    }

    private DBDataSource(DatabaseConfig dbConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbConfig.getUrl());
        config.setUsername(dbConfig.getUserName());
        config.setPassword(dbConfig.getPassword());
        config.setMinimumIdle(dbConfig.getMinPoolSize());
        config.setMaximumPoolSize(dbConfig.getMaxPoolSize());
        dataSource = new HikariDataSource(config);
    }

    public static void initialise(DatabaseConfig dbConfig) {
        INSTANCE = new DBDataSource(dbConfig);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
