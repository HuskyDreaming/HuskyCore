package com.huskydreaming.huskycore.registries;


import com.huskydreaming.huskycore.HuskyPlugin;

public interface Registry<T extends Registrable> {

    void post(HuskyPlugin plugin);

    void start(HuskyPlugin plugin);

    void stop(HuskyPlugin plugin);

    void register(Class<?> tClass, T t);

    <K extends Registrable> K provide(Class<K> c);
}