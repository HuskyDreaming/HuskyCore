package com.huskydreaming.huskycore.data;

import com.huskydreaming.huskycore.enumeration.DatabaseType;

public record DatabaseConfig(DatabaseType type, String hostname, int port, String database, String username, String password, boolean ssl, int poolSize) {

    public static DatabaseConfig defaultConfig() {
        return new DatabaseConfig(
                DatabaseType.SQLITE,
                "localhost",
                3306,
                "database",
                "username",
                "password",
                false,
                2
        );
    }
}