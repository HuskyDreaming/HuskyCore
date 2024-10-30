package com.huskydreaming.huskycore.repositories;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.registries.Registrable;

public interface Repository extends Registrable {

    default void deserialize(HuskyPlugin plugin) {

    }

    default void serialize(HuskyPlugin plugin) {

    }
}