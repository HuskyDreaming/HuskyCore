package com.huskydreaming.huskycore.interfaces.database.base;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.huskycore.utilities.AsyncAction;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface Dao<E extends SqlEntity> {
    AsyncAction<Integer> update(E entity);

    AsyncAction<Integer> insert(E entity);

    AsyncAction<Integer> delete(E entity);

    void bulkUpdate(SqlEntityType type, Collection<E> set);

    void bulkImport(SqlEntityType type, Consumer<Map<Integer, E>> callback);

    void bulkDelete(SqlEntityType type, Set<Integer> collection);
}