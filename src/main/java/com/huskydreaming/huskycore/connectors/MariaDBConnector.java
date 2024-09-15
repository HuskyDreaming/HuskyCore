package com.huskydreaming.huskycore.connectors;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.data.DatabaseConfig;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseConnector;
import com.huskydreaming.huskycore.enumeration.DatabaseType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.Connection;
import java.sql.SQLException;

public class MariaDBConnector implements DatabaseConnector {

    private final HuskyPlugin plugin;
    private final DatabaseConfig databaseConfig;

    private HikariDataSource hikariDataSource;

    public MariaDBConnector(HuskyPlugin plugin, DatabaseConfig databaseConfig) {
        this.plugin = plugin;
        this.databaseConfig = databaseConfig;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mariadb://" + databaseConfig.hostname() + ":" + databaseConfig.port() + "/" + databaseConfig.database() + "?useSSL=" + databaseConfig.ssl());
        hikariConfig.setUsername(databaseConfig.username());
        hikariConfig.setPassword(databaseConfig.password());
        hikariConfig.setMaximumPoolSize(databaseConfig.poolSize());

        try {
            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (HikariPool.PoolInitializationException e) {
            this.plugin.getLogger().severe("[Database] An error occurred while connecting to pool: " + e.getMessage());
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }

    @Override
    public void connect(ConnectionCallback callback) {
        try (Connection connection = hikariDataSource.getConnection()) {
            callback.accept(connection);
        } catch (SQLException e) {
            this.plugin.getLogger().severe("[Database] An error occurred executing MariaDB query: " + e.getMessage());
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
            plugin.getLogger().severe("[Database] An error occurred retrieving the MariaDB database connection: " + e.getMessage());
            return null;
        }
    }

    @Override
    public DatabaseConfig getConfig() {
        return databaseConfig;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.MARIADB;
    }
}
