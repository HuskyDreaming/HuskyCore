package com.huskydreaming.huskycore.interfaces.database.base;

import com.huskydreaming.huskycore.data.DatabaseConfig;
import com.huskydreaming.huskycore.enumeration.DatabaseType;

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
