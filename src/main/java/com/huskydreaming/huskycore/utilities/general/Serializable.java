package com.huskydreaming.huskycore.utilities.general;

import com.huskydreaming.huskycore.HuskyPlugin;

public interface Serializable {

    default void deserialize(HuskyPlugin plugin) {

    }

    default void serialize(HuskyPlugin plugin) {

    }
}
