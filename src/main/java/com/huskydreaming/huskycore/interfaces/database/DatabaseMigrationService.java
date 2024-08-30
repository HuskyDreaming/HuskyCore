package com.huskydreaming.huskycore.interfaces.database;

import com.huskydreaming.huskycore.interfaces.database.DatabaseConnector;
import com.huskydreaming.huskycore.interfaces.database.DatabaseMigration;
import com.huskydreaming.huskycore.interfaces.Service;

public interface DatabaseMigrationService extends Service {

    void loadMigrations(DatabaseMigration... databaseMigrations);

    void setConnector(DatabaseConnector connector);
}