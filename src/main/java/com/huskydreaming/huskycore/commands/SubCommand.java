package com.huskydreaming.huskycore.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommand extends CommandInterface {

    default String getLabel() {
        return getClass().getAnnotation(Command.class).label();
    }

    default String[] getAliases() {
        return getClass().getAnnotation(Command.class).aliases();
    }

    default String getArguments() {
        return getClass().getAnnotation(Command.class).arguments();
    }

    default List<String> onTabComplete(ConsoleCommandSender commandSender, String[] strings) {
        return null;
    }

    default List<String> onTabComplete(Player player, String[] strings) {
        return null;
    }
}
