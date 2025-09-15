package com.ecommerce.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource dataSource;
    
    static {
        try {
            Properties props = new Properties();
            InputStream input = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("database.properties");
            
            if (input == null) {
                logger.error("Unable to find database.properties file");
                throw new RuntimeException("Database configuration file not found");
            }
            
            props.load(input);
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            config.setDriverClassName(props.getProperty("db.driver"));
            
            // Connection pool settings
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.maximumPoolSize", "20")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.minimumIdle", "5")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("db.pool.connectionTimeout", "30000")));
            config.setIdleTimeout(Long.parseLong(props.getProperty("db.pool.idleTimeout", "600000")));
            config.setMaxLifetime(Long.parseLong(props.getProperty("db.pool.maxLifetime", "1800000")));
            
            // Additional settings
            config.setLeakDetectionThreshold(60000);
            config.setConnectionTestQuery("SELECT 1");
            
            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully");
            
        } catch (IOException e) {
            logger.error("Error loading database properties", e);
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Error getting database connection", e);
            throw e;
        }
    }
    
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }
    
    public static boolean isConnectionValid() {
        try (Connection conn = getConnection()) {
            return conn.isValid(5);
        } catch (SQLException e) {
            logger.error("Database connection validation failed", e);
            return false;
        }
    }
}
