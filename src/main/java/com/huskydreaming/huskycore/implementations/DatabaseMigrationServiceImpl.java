package com.huskydreaming.huskycore.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.enumeration.DatabaseType;
import com.huskydreaming.huskycore.interfaces.database.DatabaseConnector;
import com.huskydreaming.huskycore.interfaces.database.DatabaseMigration;
import com.huskydreaming.huskycore.interfaces.database.DatabaseMigrationService;
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
            PluginDescriptionFile pluginDescriptionFile = plugin.getDescription();
            String tableName = pluginDescriptionFile.getName().toLowerCase() + "_migrations";

            int currentMigrationVersion = -1;
            if (hasMigration(connection, tableName)) {
                currentMigrationVersion = retrieveMigrationVersion(connection, tableName);
            } else {
                createMigrationTable(connection, tableName);
            }

            List<DatabaseMigration> availableMigrations = retrieveMigrations(currentMigrationVersion);
            if (availableMigrations.isEmpty()) return;

            for (DatabaseMigration databaseMigration : availableMigrations) {
                databaseMigration.migrate(connection, tableName);
            }

            currentMigrationVersion = availableMigrations.stream()
                    .map(DatabaseMigration::getVersion)
                    .max(Integer::compareTo)
                    .orElse(-1);

            updateMigration(connection, tableName, currentMigrationVersion);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateMigration(Connection connection, String tableName, int currentMigration) throws SQLException {
        String updateVersion = "UPDATE " + tableName + " SET migration_version = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateVersion)) {
            statement.setInt(1, currentMigration);
            statement.execute();
        }
    }

    private List<DatabaseMigration> retrieveMigrations(int finalVersion) {
        return migrations.stream()
                .filter(m -> m.getVersion() > finalVersion)
                .sorted(Comparator.comparingInt(DatabaseMigration::getVersion))
                .collect(Collectors.toList());
    }

    private int retrieveMigrationVersion(Connection connection, String tableName) throws SQLException {
        int migrationVersion;
        String selectVersion = "SELECT migration_version FROM " + tableName;
        try (PreparedStatement statement = connection.prepareStatement(selectVersion)) {
            ResultSet result = statement.executeQuery();
            result.next();
            migrationVersion = result.getInt("migration_version");
        }
        return migrationVersion;
    }

    private void createMigrationTable(Connection connection, String tableName) throws SQLException {
        String createTable = "CREATE TABLE " + tableName + " (migration_version INT NOT NULL)";
        try (PreparedStatement statement = connection.prepareStatement(createTable)) {
            statement.execute();
        }

        // Insert primary row into migration table
        String insertRow = "INSERT INTO " + tableName + " VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(insertRow)) {
            statement.setInt(1, -1);
            statement.execute();
        }
    }

    private boolean hasMigration(Connection connection, String tableName) {
        String query = connector.getType() == DatabaseType.SQLITE ?
                "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?" :
                "SHOW TABLES LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }
}