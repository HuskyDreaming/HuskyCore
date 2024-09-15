package com.huskydreaming.huskycore.implementations;

import com.google.common.reflect.TypeToken;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.connectors.MariaDBConnector;
import com.huskydreaming.huskycore.connectors.MySQLConnector;
import com.huskydreaming.huskycore.connectors.SQLiteConnector;
import com.huskydreaming.huskycore.data.DatabaseConfig;
import com.huskydreaming.huskycore.enumeration.DatabaseType;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseConnector;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseService;
import com.huskydreaming.huskycore.storage.Json;
import org.bukkit.Bukkit;

import java.lang.reflect.Type;
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

        Type type = new TypeToken<DatabaseConfig>(){}.getType();
        DatabaseConfig databaseConfig = Json.read(plugin, "database", type);
        DatabaseType databaseType = databaseConfig != null ? databaseConfig.type() : DatabaseType.SQLITE;

        if(databaseConfig == null) {
            databaseConfig = DatabaseConfig.defaultConfig();
            Json.write(plugin, "database", databaseConfig);
        }

        this.databaseConnector = switch (databaseType) {
            case SQLITE -> new SQLiteConnector(plugin, databaseConfig);
            case MYSQL -> new MySQLConnector(plugin, databaseConfig);
            case MARIADB -> new MariaDBConnector(plugin, databaseConfig);
        };
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        databaseConnector.connect(connection -> {
            if(connection != null) {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                plugin.getLogger().info("[Database] Using driver " + databaseMetaData.getDriverName());
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
    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
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