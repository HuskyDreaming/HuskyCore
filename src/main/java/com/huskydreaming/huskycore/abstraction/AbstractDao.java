package com.huskydreaming.huskycore.abstraction;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.enumeration.SqlOption;
import com.huskydreaming.huskycore.interfaces.database.Dao;
import com.huskydreaming.huskycore.interfaces.database.DatabaseConnector;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.huskycore.utilities.AsyncAction;
import com.huskydreaming.huskycore.utilities.Query;
import org.bukkit.Bukkit;

import java.io.Serializable;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AbstractDao<E extends SqlEntity & Serializable> implements Dao<E> {

    private final HuskyPlugin plugin;
    private final DatabaseConnector databaseConnector;

    public AbstractDao(HuskyPlugin plugin) {
        this.plugin = plugin;
        this.databaseConnector = plugin.getDatabaseConnector();
    }

    public abstract int setStatement(PreparedStatement statement, E e) throws SQLException;

    public abstract  E fromResult(ResultSet result) throws SQLException;

    @Override
    public AsyncAction<Integer> insert(E entry) {
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseConnector.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.insert(entry.getEntityType()))) {
                    setStatement(statement, entry);
                    statement.executeUpdate();
                    if (statement.executeUpdate() > 0) {
                        ResultSet result = statement.getGeneratedKeys();
                        if (result.next()) return result.getInt(1);
                    }
                }
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
            return 0;
        });
    }

    @Override
    public AsyncAction<Integer> update(E entry) {
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseConnector.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.update(entry.getEntityType()))) {
                    int index = setStatement(statement, entry);
                    statement.setInt(index, entry.getId());
                    statement.executeUpdate();
                    if (statement.executeUpdate() > 0) {
                        ResultSet result = statement.getGeneratedKeys();
                        if (result.next()) return result.getInt(1);
                    }
                }
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
            return 0;
        });
    }

    @Override
    public AsyncAction<Integer> delete(E entry) {
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseConnector.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.delete(entry.getEntityType()))) {
                    statement.setInt(1, entry.getId());
                    statement.executeUpdate();
                    if (statement.executeUpdate() > 0) {
                        ResultSet result = statement.getGeneratedKeys();
                        if (result.next()) return result.getInt(1);
                    }
                }
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
            return 0;
        });
    }

    @Override
    public void bulkUpdate(SqlEntityType type, Set<E> set) {
        plugin.runAndWait(() -> {
            try (Connection connection = databaseConnector.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.update(type))) {
                    for (var entry : set) {
                        int index = setStatement(statement, entry);
                        statement.setInt(index, entry.getId());
                        statement.addBatch();
                    }
                    statement.executeBatch();
                }
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public void bulkImport(SqlEntityType type, Consumer<Set<E>> callback) {
        plugin.runAsync(() -> {
            Set<E> entries = new HashSet<>();
            try (Connection connection = databaseConnector.getConnection()) {
                var query = Query.select(type, SqlOption.EMPTY);
                try (Statement statement = connection.createStatement()) {
                    ResultSet result = statement.executeQuery(query);
                    while (result.next()) entries.add(fromResult(result));
                    Bukkit.getScheduler().runTask(this.plugin, () -> callback.accept(entries));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void bulkDelete(SqlEntityType type, Set<E> set) {
        plugin.runAndWait(() -> {
            try (Connection connection = databaseConnector.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.delete(type))) {
                    for (var entry : set) {
                        statement.setInt(1, entry.getId());
                        statement.addBatch();
                    }
                    statement.executeBatch();
                }
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }
}