package com.huskydreaming.huskycore.registries;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Registry;
import com.huskydreaming.huskycore.interfaces.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry implements Registry {

    private final Map<Class<?>, Service> services = new ConcurrentHashMap<>();

    @Override
    public void deserialize(HuskyPlugin plugin) {
        services.values().forEach(s -> s.deserialize(plugin));
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        services.values().forEach(s -> s.serialize(plugin));
    }

    public <T> T provide(Class<T> tClass) {
        return tClass.cast(services.get(tClass));
    }

    public void register(Class<?> tClass, Service service) {
        services.put(tClass, service);
    }
}