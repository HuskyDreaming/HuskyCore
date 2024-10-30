package com.huskydreaming.huskycore.handlers.interfaces;

import com.huskydreaming.huskycore.database.connectors.DatabaseConnector;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

public interface DatabaseHandler extends Handler {

    Connection getConnection();

    DatabaseConnector getDatabaseConnector();

    CompletableFuture<Void> asyncFuture(Runnable runnable);

    void runSync(Runnable runnable);

    void runAsync(Runnable runnable);
}