package com.huskydreaming.huskycore.connectors;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.database.DatabaseConnector;
import com.huskydreaming.huskycore.enumeration.DatabaseType;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector implements DatabaseConnector {

    private final HuskyPlugin plugin;
    private final String string;
    private Connection connection;

    public SQLiteConnector(HuskyPlugin plugin) {
        PluginDescriptionFile pluginDescriptionFile = plugin.getDescription();
        String pluginName = pluginDescriptionFile.getName();

        this.plugin = plugin;
        this.string = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + pluginName.toLowerCase() + ".db";
    }

    @Override
    public void connect(ConnectionCallback callback) {
        try {
            callback.accept(getConnection());
        } catch (Exception ex) {
            this.plugin.getLogger().severe("An error occurred executing an SQLite query: " + ex.getMessage());
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("An error occurred while closing the SQLite database connection: " + e.getMessage());
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(string);
                plugin.getLogger().info("Connection to SQLite database established");
            }
        } catch (SQLException e) {
            this.plugin.getLogger().severe("An error occurred retrieving the SQLite database connection: " + e.getMessage());
        }
        return connection;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.SQLITE;
    }
}
