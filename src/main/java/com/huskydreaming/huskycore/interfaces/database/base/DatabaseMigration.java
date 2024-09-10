package com.huskydreaming.huskycore.interfaces.database.base;

import java.sql.Connection;

public interface DatabaseMigration {

    void migrate(Connection connection, String tablePrefix);

    int getVersion();
}
