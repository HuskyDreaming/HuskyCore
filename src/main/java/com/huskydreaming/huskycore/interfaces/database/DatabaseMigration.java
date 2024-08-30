package com.huskydreaming.huskycore.interfaces.database;

import java.sql.Connection;

public interface DatabaseMigration {

    void migrate(Connection connection, String tablePrefix);

    int getVersion();
}
