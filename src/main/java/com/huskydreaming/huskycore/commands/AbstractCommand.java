package com.huskydreaming.huskycore.commands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Parseable;
import com.huskydreaming.huskycore.registries.CommandRegistry;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandExecutor, CommandInterface, TabCompleter {

    private final String name;
    private final CommandRegistry commandRegistry;

    public AbstractCommand(String name, HuskyPlugin huskyPlugin) {
        this.name = name;
        this.commandRegistry = huskyPlugin.getCommandRegistry();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            SubCommand subCommand = commandRegistry.getSubCommand(strings[0]);
            if (subCommand == null) {
                commandSender.sendMessage(getUnknownSubCommandLocale().prefix(strings[0]));
                return true;
            }
            if(commandSender instanceof ConsoleCommandSender sender) {
                subCommand.run(sender, strings);
                return true;
            }
            if (commandSender.hasPermission(name + "." + subCommand.getLabel().toLowerCase()) || commandSender.isOp()) {
                if(commandSender instanceof Player player) subCommand.run(player, strings);
            } else {
                commandSender.sendMessage(getPermissionsLocale().prefix());
            }
        } else {
            if(commandSender instanceof ConsoleCommandSender sender) run(sender, strings);
            if(commandSender instanceof Player player) run(player, strings);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) return commandRegistry.getSubCommands().stream().map(c -> c.getLabel().toLowerCase()).collect(Collectors.toList());
        if( strings.length > 1) {
            SubCommand subCommand = commandRegistry.getSubCommand(strings[0].toLowerCase());
            if (subCommand != null) {
                if(commandSender instanceof ConsoleCommandSender sender) return subCommand.onTabComplete(sender, strings);
                if(commandSender instanceof Player player) return subCommand.onTabComplete(player, strings);
            }
        }
        return List.of();
    }

    public String getName() {
        return name;
    }

    public abstract Parseable getPermissionsLocale();

    public abstract Parseable getUnknownSubCommandLocale();
}