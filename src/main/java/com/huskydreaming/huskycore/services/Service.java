package com.huskydreaming.huskycore.services;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.registries.Registrable;

public interface Service extends Registrable {

    default void load(HuskyPlugin plugin) {

    }

    default void postLoad(HuskyPlugin plugin) {

    }

    default void unload(HuskyPlugin plugin) {

    }
}