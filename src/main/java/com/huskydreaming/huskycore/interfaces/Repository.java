package com.huskydreaming.huskycore.interfaces;

import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Repository<E extends SqlEntity> {

    void add(E entity);
    void bulkAdd(Map<Long, E> entities);
    void remove(E entity);

    E get(long id);
    Map<Long, E> all();
    Collection<E> values();
    Set<Long> keys();
    Set<Map.Entry<Long,E>> entries();
    int size();
}
