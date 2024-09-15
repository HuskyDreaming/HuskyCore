package com.huskydreaming.huskycore.registries;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Registry;
import com.huskydreaming.huskycore.interfaces.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

public class ServiceRegistry implements Registry {

    private final Map<Class<?>, Service> services = new LinkedHashMap<>();

    @Override
    public void deserialize(HuskyPlugin plugin) {
        synchronized(services) {
            try {
                services.values().forEach(s -> s.deserialize(plugin));
                plugin.getLogger().info("[Services] Successfully deserialized all services.");
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        synchronized(services) {
            try {
                ListIterator<Service> iterator = new ArrayList<>(services.values()).listIterator(services.size());
                while (iterator.hasPrevious()) iterator.previous().serialize(plugin);
                plugin.getLogger().info("[Services] Successfully serialized all services.");
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public <T> T provide(Class<T> tClass) {
        return tClass.cast(services.get(tClass));
    }

    public void register(Class<?> tClass, Service service) {
        services.put(tClass, service);
    }
}