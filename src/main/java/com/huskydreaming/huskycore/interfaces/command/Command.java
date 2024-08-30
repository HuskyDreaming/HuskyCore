package com.huskydreaming.huskycore.interfaces.command;

import com.huskydreaming.huskycore.annotations.CommandAnnotation;

public interface Command {

    default String getLabel() {
        return getClass().getAnnotation(CommandAnnotation.class).label();
    }

    default String[] getArguments() {
        return getClass().getAnnotation(CommandAnnotation.class).arguments();
    }
}
