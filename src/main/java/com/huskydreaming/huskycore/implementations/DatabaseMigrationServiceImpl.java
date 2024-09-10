package com.huskydreaming.huskycore.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.enumeration.DatabaseType;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseConnector;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseMigration;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseMigrationService;
import org.bukkit.plugin.PluginDescriptionFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseMigrationServiceImpl implements DatabaseMigrationService {

    private List<DatabaseMigration> migrations;
    private DatabaseConnector connector;

    @Override
    public void loadMigrations(DatabaseMigration... migrations) {
        this.migrations = List.of(migrations);
    }

    @Override
    public void setConnector(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        try (Connection connection = connector.getConnection()) {
            PluginDescriptionFile descriptionFile = plugin.getDescription();
            String pluginName = descriptionFile.getName().toLowerCase();

            int currentMigrationVersion = 0;
            if (hasMigration(connection, pluginName)) {
                currentMigrationVersion = retrieveMigrationVersion(connection, pluginName);
            } else {
                createMigrationTable(connection, pluginName);
            }

            List<DatabaseMigration> availableMigrations = retrieveMigrations(currentMigrationVersion);
            if (availableMigrations.isEmpty()) {
                plugin.getLogger().info("[Database] Currently on latest version v" + currentMigrationVersion);
                return;
            }

            plugin.getLogger().info("[Database] Currently " + availableMigrations.size() + " version(s) behind");
            for (DatabaseMigration databaseMigration : availableMigrations) {
                databaseMigration.migrate(connection, pluginName);
                plugin.getLogger().info("[Database] Migrating to version v" + databaseMigration.getVersion());
            }

            currentMigrationVersion = availableMigrations.stream()
                    .map(DatabaseMigration::getVersion)
                    .max(Integer::compareTo)
                    .orElse(0);

            updateMigration(connection, pluginName, currentMigrationVersion);
            plugin.getLogger().info("[Database] Successfully updated to the latest version v" + currentMigrationVersion);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMigration(Connection connection, String pluginName, int currentMigration) throws SQLException {
        String updateVersion = "UPDATE " + pluginName + "_migrations SET migration_version = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateVersion)) {
            statement.setInt(1, currentMigration);
            statement.execute();
        }
    }

    @Override
    public List<DatabaseMigration> retrieveMigrations(int finalVersion) {
        return migrations.stream()
                .filter(m -> m.getVersion() > finalVersion)
                .sorted(Comparator.comparingInt(DatabaseMigration::getVersion))
                .collect(Collectors.toList());
    }

    @Override
    public int retrieveMigrationVersion(Connection connection, String pluginName) throws SQLException {
        int migrationVersion;
        String selectVersion = "SELECT migration_version FROM " + pluginName + "_migrations";
        try (PreparedStatement statement = connection.prepareStatement(selectVersion)) {
            ResultSet result = statement.executeQuery();
            result.next();
            migrationVersion = result.getInt("migration_version");
        }
        return migrationVersion;
    }

    @Override
    public void createMigrationTable(Connection connection, String pluginName) throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS " + pluginName + "_migrations (migration_version INT NOT NULL)";
        try (PreparedStatement statement = connection.prepareStatement(createTable)) {
            statement.execute();
        }

        // Insert primary row into migration table
        String insertRow = "INSERT INTO " + pluginName + "_migrations VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(insertRow)) {
            statement.setInt(1, -1);
            statement.execute();
        }
    }

    @Override
    public boolean hasMigration(Connection connection, String pluginName) {
        String query = connector.getType() == DatabaseType.SQLITE ?
                "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?" : // SQLITE
                "SHOW TABLES LIKE ?";                                             // MYSQL OR MARIADB

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pluginName + "_migrations");
            return statement.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }
}