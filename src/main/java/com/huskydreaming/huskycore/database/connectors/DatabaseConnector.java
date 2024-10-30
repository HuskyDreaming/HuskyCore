package com.huskydreaming.huskycore.database.connectors;

import com.huskydreaming.huskycore.database.base.DatabaseConfig;
import com.huskydreaming.huskycore.database.base.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {

    void connect(ConnectionCallback callback);

    void close();

    Connection getConnection();

    interface ConnectionCallback {
        void accept(Connection connection) throws SQLException;
    }

    DatabaseConfig getConfig();

    DatabaseType getType();
}