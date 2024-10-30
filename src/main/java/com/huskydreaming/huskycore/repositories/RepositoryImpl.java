package com.huskydreaming.huskycore.repositories;

import com.huskydreaming.huskycore.database.sql.SqlEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


// INFO: Created this class and interface in case I am going to add redis cache
// for now we can just use concurrent hashmap
public class RepositoryImpl<E extends SqlEntity> implements SqlRepository<E> {

    private final Map<Long, E> entities = new ConcurrentHashMap<>();

    @Override
    public void add(E entity) {
        entities.put(entity.getId(), entity);
    }

    @Override
    public void bulkAdd(Map<Long, E> entities) {
        this.entities.putAll(entities);
    }

    @Override
    public void remove(E entity) {
        entities.remove(entity.getId());
    }

    @Override
    public E get(long id) {
        return entities.get(id);
    }

    @Override
    public Map<Long, E> all() {
        return Collections.unmodifiableMap(entities);
    }

    @Override
    public Collection<E> values() {
        return entities.values();
    }

    @Override
    public Set<Long> keys() {
        return entities.keySet();
    }

    @Override
    public Set<Map.Entry<Long,E>> entries() {
        return entities.entrySet();
    }

    @Override
    public int size() {
        return entities.size();
    }
}