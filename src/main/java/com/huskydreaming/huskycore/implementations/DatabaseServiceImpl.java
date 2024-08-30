package com.huskydreaming.huskycore.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.connectors.MariaDBConnector;
import com.huskydreaming.huskycore.connectors.MySQLConnector;
import com.huskydreaming.huskycore.connectors.SQLiteConnector;
import com.huskydreaming.huskycore.data.DatabaseConfig;
import com.huskydreaming.huskycore.enumeration.DatabaseType;
import com.huskydreaming.huskycore.interfaces.database.DatabaseConnector;
import com.huskydreaming.huskycore.interfaces.database.DatabaseService;
import com.huskydreaming.huskycore.storage.Json;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseServiceImpl implements DatabaseService {

    private final HuskyPlugin plugin;
    private final DatabaseConnector databaseConnector;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DatabaseServiceImpl(HuskyPlugin plugin) {
        this.plugin = plugin;

        DatabaseConfig databaseConfig = Json.read(plugin, "database", DatabaseConfig.class);
        DatabaseType databaseType = databaseConfig != null ? databaseConfig.type() : DatabaseType.SQLITE;
        if(databaseConfig == null) Json.write(plugin, "database", DatabaseConfig.defaultConfig());

        this.databaseConnector = switch (databaseType) {
            case SQLITE -> new SQLiteConnector(plugin);
            case MYSQL -> new MySQLConnector(plugin, databaseConfig);
            case MARIADB -> new MariaDBConnector(plugin, databaseConfig);
        };
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        databaseConnector.connect(connection -> {
            if(connection != null) {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                plugin.getLogger().info("The driver name is " + databaseMetaData.getDriverName());
                plugin.getLogger().info("A new database has been created.");
            }
        });
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        databaseConnector.close();
        executorService.shutdown();
    }

    @Override
    public Connection getConnection() {
        return databaseConnector.getConnection();
    }

    @Override
    public CompletableFuture<Void> asyncFuture(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executorService);
    }

    @Override
    public void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void runAsync(Runnable runnable) {
        executorService.execute(runnable);
    }
}