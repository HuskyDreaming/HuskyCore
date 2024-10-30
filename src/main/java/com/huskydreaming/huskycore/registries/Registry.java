package com.huskydreaming.huskycore.registries;


import com.huskydreaming.huskycore.HuskyPlugin;

public interface Registry<T extends Registrable> {

    void start(HuskyPlugin plugin, RegistryType registryType);

    void shutdown(HuskyPlugin plugin);

    void register(Class<?> tClass, T t);

    <K extends Registrable> K provide(Class<K> c);
}