package com.huskydreaming.huskycore.interfaces.database.base;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.huskycore.utilities.AsyncAction;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface Dao<E extends SqlEntity> {
    AsyncAction<Long> update(E entity);

    void insertStatement(E entity, Connection connection, Consumer<Long> consumer);

    AsyncAction<Long> insert(E entity);

    AsyncAction<Boolean> delete(E entity);

    void bulkUpdate(SqlEntityType type, Collection<E> set);

    void bulkImport(SqlEntityType type, Consumer<Map<Long, E>> callback);

    void bulkDelete(SqlEntityType type, Set<Long> collection);
}