package com.huskydreaming.huskycore.abstraction;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.database.base.Dao;
import com.huskydreaming.huskycore.interfaces.database.base.DatabaseService;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.huskycore.utilities.AsyncAction;
import com.huskydreaming.huskycore.utilities.Query;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public abstract class AbstractDao<E extends SqlEntity> implements Dao<E> {

    protected final HuskyPlugin plugin;
    protected final DatabaseService databaseService;
    protected final String pluginName;

    public AbstractDao(HuskyPlugin plugin) {
        this.plugin = plugin;
        this.pluginName = plugin.getDescription().getName().toLowerCase();
        this.databaseService = plugin.provide(DatabaseService.class);
    }

    public abstract int setStatement(PreparedStatement statement, E entity) throws SQLException;

    public abstract E fromResult(ResultSet result) throws SQLException;

    @Override
    public void insertStatement(E entity, Connection connection, Consumer<Long> consumer) {
        String query = Query.insert(entity.getEntityType(), pluginName);
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(statement, entity);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("[Database] Inserting record for " + entity.getEntityType().toTable() + " table failed, no rows affected");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    consumer.accept(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("[Database] Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AsyncAction<Long> insert(E entity) {
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseService.getConnection()) {
                String query = Query.insert(entity.getEntityType(), pluginName);
                try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    setStatement(statement, entity);
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("[Database] Inserting record for " + entity.getEntityType().toTable() + " table failed, no rows affected");
                    }
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getLong(1);
                        } else {
                            throw new SQLException("[Database] Creating user failed, no ID obtained.");
                        }
                    }
                }
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public AsyncAction<Long> update(E entity) {
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseService.getConnection()) {
                String query = Query.update(entity.getEntityType(), pluginName);
                try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    int index = setStatement(statement, entity);
                    statement.setLong(index, entity.getId());
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("[Database] Updating record for " + entity.getEntityType().toTable() + " table failed, no rows affected.");
                    }
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getLong(1);
                        } else {
                            throw new SQLException("[Database] Creating user failed, no id obtained.");
                        }
                    }
                }
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public AsyncAction<Boolean> delete(E entity) {
        AtomicBoolean success = new AtomicBoolean(false);
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseService.getConnection()) {
                String query = Query.delete(entity.getEntityType(), pluginName);
                try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setLong(1, entity.getId());
                    int affectedRows = statement.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("[Database] Deleting record for " + entity.getEntityType().toTable() + " table failed, no rows affected.");
                    }
                    success.set(true);
                }
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
            return success.get();
        });
    }

    @Override
    public void bulkUpdate(SqlEntityType type, Collection<E> collection) {
        plugin.runAndWait(() -> {
            try (Connection connection = databaseService.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.update(type, pluginName))) {
                    for (var value : collection) {
                        int index = setStatement(statement, value);
                        statement.setLong(index, value.getId());
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
    public void bulkImport(SqlEntityType type, Consumer<Map<Long, E>> callback) {
        plugin.runAsync(() -> {
            Map<Long, E> map = new ConcurrentHashMap<>();
            try (Connection connection = databaseService.getConnection()) {
                var query = Query.selectEmpty(type, pluginName);
                try (Statement statement = connection.createStatement()) {
                    ResultSet result = statement.executeQuery(query);
                    while (result.next()) {
                        E element = fromResult(result);
                        map.put(element.getId(), element);
                    }
                    result.close();
                    Bukkit.getScheduler().runTask(this.plugin, () -> callback.accept(map));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void bulkDelete(SqlEntityType type, Set<Long> collection) {
        plugin.runAndWait(() -> {
            try (Connection connection = databaseService.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.delete(type, pluginName))) {
                    for (var value : collection) {
                        statement.setLong(1, value);
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