package com.huskydreaming.huskycore.enumeration;

import com.huskydreaming.huskycore.utilities.Query;

public enum SqlOption {
    EMPTY(""),
    WHERE(Query.WHERE);

    private final String string;
    SqlOption(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
