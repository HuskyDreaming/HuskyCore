package com.huskydreaming.huskycore.interfaces.database;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;
import com.huskydreaming.huskycore.utilities.AsyncAction;

import java.io.Serializable;
import java.util.Set;
import java.util.function.Consumer;

public interface Dao<T extends SqlEntity & Serializable> {
    AsyncAction<Integer> update(T t);

    AsyncAction<Integer> insert(T entry);

    AsyncAction<Integer> delete(T entry);

    void bulkUpdate(SqlEntityType type, Set<T> set);

    void bulkImport(SqlEntityType type, Consumer<Set<T>> callback);

    void bulkDelete(SqlEntityType type, Set<T> set);
}