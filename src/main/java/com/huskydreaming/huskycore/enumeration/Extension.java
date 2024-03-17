package com.huskydreaming.huskycore.enumeration;

public enum Extension {
    JSON(".json"),
    YAML(".yml");

    private final String string;

    Extension(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}