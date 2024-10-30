package com.huskydreaming.huskycore.commands;

import com.huskydreaming.huskycore.commands.annotations.CommandAnnotation;

public interface Command {

    default String getLabel() {
        return getClass().getAnnotation(CommandAnnotation.class).label();
    }

    default String[] getArguments() {
        return getClass().getAnnotation(CommandAnnotation.class).arguments();
    }
}