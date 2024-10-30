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

    @Override
    public void start(HuskyPlugin plugin, RegistryType registryType) {
        synchronized(registries) {
            try {
                registries.values().forEach(r -> {
                    switch (registryType) {
                        case POST -> {
                            if (r instanceof Service service) {
                                service.postLoad(plugin);
                            } else if (r instanceof Repository repository) {
                                repository.postDeserialize(plugin);
                            } else if (r instanceof Handler handler) {
                                handler.postInitialize(plugin);
                            }
                        }
                        case STARTUP -> {
                            if (r instanceof Service service) {
                                service.load(plugin);
                            } else if (r instanceof Repository repository) {
                                repository.deserialize(plugin);
                            } else if (r instanceof Handler handler) {
                                handler.initialize(plugin);
                            }
                        }
                    }
                });
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public void shutdown(HuskyPlugin plugin) {
        synchronized(registries) {
            try {
                ListIterator<T> iterator = new ArrayList<>(registries.values()).listIterator(registries.size());
                while (iterator.hasPrevious()) {
                    T type = iterator.previous();
                    if(type instanceof Service service) {
                        service.unload(plugin);
                    } else if(type instanceof Repository repository) {
                        repository.serialize(plugin);
                    } else if(type instanceof Handler handler) {
                        handler.finalize(plugin);
                    }
                }
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