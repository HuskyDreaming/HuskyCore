package com.huskydreaming.huskycore.commands;

import java.util.List;

public interface SubCommand<T> extends Command {

    void onCommand(T t, String[] strings);

    default List<String> onTabComplete(T t, String[] strings) {
        return List.of();
    }
}