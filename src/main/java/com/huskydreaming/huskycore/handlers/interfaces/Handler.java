package com.huskydreaming.huskycore.handlers.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.registries.Registrable;

public interface Handler extends Registrable {

    default void initialize(HuskyPlugin plugin) {

    }

    default void postInitialize(HuskyPlugin plugin) {

    }

    default void finalize(HuskyPlugin plugin) {

    }
}