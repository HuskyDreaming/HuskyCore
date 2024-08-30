package com.huskydreaming.huskycore.connectors;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.data.DatabaseConfig;
import com.huskydreaming.huskycore.interfaces.database.DatabaseConnector;
import com.huskydreaming.huskycore.enumeration.DatabaseType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnector implements DatabaseConnector {

    private final HuskyPlugin plugin;
    private final HikariDataSource hikariDataSource;

    public MySQLConnector(HuskyPlugin plugin, DatabaseConfig databaseConfig) {
        this.plugin = plugin;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + databaseConfig.hostname() + ":" + databaseConfig.port() + "/" + databaseConfig.database() + "?useSSL=" + databaseConfig.ssl());
        hikariConfig.setUsername(databaseConfig.username());
        hikariConfig.setPassword(databaseConfig.password());
        hikariConfig.setMaximumPoolSize(databaseConfig.poolSize());

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public void connect(ConnectionCallback callback) {
        try (Connection connection = hikariDataSource.getConnection()) {
            callback.accept(connection);
        } catch (SQLException e) {
            this.plugin.getLogger().severe("An error occurred executing MySQL query: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        hikariDataSource.close();
    }

    @Override
    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            plugin.getLogger().severe("An error occurred retrieving the MariaDB database connection: " + e.getMessage());
            return null;
        }
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.MYSQL;
    }
}
