package com.huskydreaming.huskycore.registries;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.handlers.interfaces.Handler;
import com.huskydreaming.huskycore.repositories.Repository;
import com.huskydreaming.huskycore.services.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

public class RegistryImpl<T extends Registrable> implements Registry<T> {

    private final Map<Class<?>, T> registries = new LinkedHashMap<>();
    private final static String PREFIX = "[Registry]";

    @Override
    public void post(HuskyPlugin plugin) {
        synchronized(registries) {
            try {
                registries.values().forEach(r -> {
                    if(r instanceof Handler handler) {
                        handler.postInitialize(plugin);
                    }
                });
                plugin.getLogger().info(PREFIX + " Successfully ran post startup for registries");
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public void start(HuskyPlugin plugin) {
        synchronized(registries) {
            try {
                ListIterator<T> iterator = new ArrayList<>(registries.values()).listIterator(registries.size());
                while (iterator.hasPrevious()) {
                    if(iterator.previous() instanceof Service service) {
                        service.load(plugin);
                    }

                    if(iterator.previous() instanceof Repository repository) {
                        repository.serialize(plugin);
                    }

                    if(iterator.previous() instanceof Handler handler) {
                        handler.initialize(plugin);
                    }
                }
                plugin.getLogger().info(PREFIX + " Successfully started all registries");
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public void stop(HuskyPlugin plugin) {
        synchronized(registries) {
            try {
                ListIterator<T> iterator = new ArrayList<>(registries.values()).listIterator(registries.size());
                while (iterator.hasPrevious()) {
                    if(iterator.previous() instanceof Service service) {
                        service.unload(plugin);
                    }

                    if(iterator.previous() instanceof Repository repository) {
                        repository.deserialize(plugin);
                    }

                    if(iterator.previous() instanceof Handler handler) {
                        handler.finalize(plugin);
                    }
                }
                plugin.getLogger().info(PREFIX + " Successfully stopped all registries");
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public void register(Class<?> tClass, T t) {
        registries.put(tClass, t);
    }

    @Override
    public <K extends Registrable> K provide(Class<K> c) {
        return c.cast(registries.get(c));
    }
}