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
    public AsyncAction<Integer> insert(E entity) {
        plugin.getLogger().info(Query.insert(entity.getEntityType(), pluginName));
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseService.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.insert(entity.getEntityType(), pluginName))) {
                    setStatement(statement, entity);
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
    public AsyncAction<Integer> update(E entity) {
        plugin.getLogger().info(Query.update(entity.getEntityType(), pluginName));
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseService.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.update(entity.getEntityType(), pluginName))) {
                    int id = setStatement(statement, entity);
                    statement.setInt(id, entity.getId());
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
    public AsyncAction<Integer> delete(E entity) {
        plugin.getLogger().info(Query.update(entity.getEntityType(), pluginName));
        return AsyncAction.supplyAsync(plugin, () -> {
            try (Connection connection = databaseService.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.delete(entity.getEntityType(), pluginName))) {
                    statement.setInt(1, entity.getId());
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
    public void bulkUpdate(SqlEntityType type, Collection<E> collection) {
        plugin.runAndWait(() -> {
            try (Connection connection = databaseService.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.update(type, pluginName))) {
                    for (var value : collection) {
                        int id = setStatement(statement, value);
                        statement.setInt(id, value.getId());
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
    public void bulkImport(SqlEntityType type, Consumer<Map<Integer, E>> callback) {
        plugin.runAsync(() -> {
            Map<Integer, E> map = new ConcurrentHashMap<>();
            try (Connection connection = databaseService.getConnection()) {
                var query = Query.selectEmpty(type, pluginName);
                try (Statement statement = connection.createStatement()) {
                    ResultSet result = statement.executeQuery(query);
                    while (result.next()) {
                        E element = fromResult(result);
                        map.put(element.getId(), element);
                    }
                    Bukkit.getScheduler().runTask(this.plugin, () -> callback.accept(map));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void bulkDelete(SqlEntityType type, Set<Integer> collection) {
        plugin.runAndWait(() -> {
            try (Connection connection = databaseService.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(Query.delete(type, pluginName))) {
                    for (var value : collection) {
                        statement.setInt(1, value);
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