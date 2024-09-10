package com.huskydreaming.huskycore.interfaces.database.base;

import com.huskydreaming.huskycore.interfaces.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseMigrationService extends Service {

    void loadMigrations(DatabaseMigration... databaseMigrations);

    void setConnector(DatabaseConnector connector);

    void updateMigration(Connection connection, String pluginName, int currentMigration) throws SQLException;

    List<DatabaseMigration> retrieveMigrations(int finalVersion);

    int retrieveMigrationVersion(Connection connection, String pluginName) throws SQLException;

    void createMigrationTable(Connection connection, String pluginName) throws SQLException;

    boolean hasMigration(Connection connection, String pluginName);
}