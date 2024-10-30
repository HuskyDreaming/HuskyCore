package com.huskydreaming.huskycore.handlers.interfaces;

import com.huskydreaming.huskycore.database.base.DatabaseMigration;
import com.huskydreaming.huskycore.database.connectors.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseMigrationHandler extends Handler {

    void loadMigrations(DatabaseMigration... databaseMigrations);

    void setConnector(DatabaseConnector connector);

    void updateMigration(Connection connection, String pluginName, int currentMigration) throws SQLException;

    List<DatabaseMigration> retrieveMigrations(int finalVersion);

    int retrieveMigrationVersion(Connection connection, String pluginName) throws SQLException;

    void createMigrationTable(Connection connection, String pluginName) throws SQLException;

    boolean hasMigration(Connection connection, String pluginName);
}