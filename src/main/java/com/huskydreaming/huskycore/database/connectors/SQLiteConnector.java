package com.huskydreaming.huskycore.database.connectors;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.database.base.DatabaseConfig;
import com.huskydreaming.huskycore.database.base.DatabaseType;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector implements DatabaseConnector {

    private final HuskyPlugin plugin;
    private final String string;
    private Connection connection;
    private final DatabaseConfig databaseConfig;

    public SQLiteConnector(HuskyPlugin plugin, DatabaseConfig databaseConfig) {
        this.plugin = plugin;
        this.databaseConfig = databaseConfig;
        this.string = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + databaseConfig.database() + ".db";
    }

    @Override
    public void connect(ConnectionCallback callback) {
        try {
            callback.accept(getConnection());
        } catch (Exception ex) {
            this.plugin.getLogger().severe("[Database] An error occurred executing an SQLite query: " + ex.getMessage());
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("[Database] An error occurred while closing the SQLite database connection: " + e.getMessage());
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(string);
            }
        } catch (SQLException e) {
            this.plugin.getLogger().severe("[Database] An error occurred retrieving the SQLite database connection: " + e.getMessage());
        }
        return connection;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.SQLITE;
    }

    @Override
    public DatabaseConfig getConfig() {
        return databaseConfig;
    }
}