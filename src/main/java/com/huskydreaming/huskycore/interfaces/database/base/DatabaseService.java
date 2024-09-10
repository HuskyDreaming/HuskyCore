package com.huskydreaming.huskycore.interfaces.database.base;

import com.huskydreaming.huskycore.interfaces.Service;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

public interface DatabaseService extends Service {

    Connection getConnection();

    DatabaseConnector getDatabaseConnector();

    CompletableFuture<Void> asyncFuture(Runnable runnable);

    void runSync(Runnable runnable);

    void runAsync(Runnable runnable);
}