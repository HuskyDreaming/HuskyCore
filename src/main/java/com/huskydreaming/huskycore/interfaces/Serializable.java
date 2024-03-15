package com.huskydreaming.huskycore.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;

public interface Serializable {

    default void deserialize(HuskyPlugin plugin) {

    }

    default void serialize(HuskyPlugin plugin) {

    }
}
