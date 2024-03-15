package com.huskydreaming.huskycore.commands;

import org.bukkit.command.CommandSender;
import java.util.List;

public interface SubCommand {

    default String getLabel() {
        return getClass().getAnnotation(Command.class).label();
    }

    default String[] getAliases() {
        return getClass().getAnnotation(Command.class).aliases();
    }

    default String getArguments() {
        return getClass().getAnnotation(Command.class).arguments();
    }

    void run(CommandSender sender, String[] strings);

    List<String> onTabComplete(String[] strings);
}
