package com.huskydreaming.huskycore.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public interface CommandInterface {

    default void run(ConsoleCommandSender sender, String[] strings) {

    }

    default void run(Player player, String[] strings) {

    }
}
