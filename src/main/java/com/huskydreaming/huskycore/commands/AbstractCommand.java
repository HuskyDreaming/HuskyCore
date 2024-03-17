package com.huskydreaming.huskycore.commands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Parseable;
import com.huskydreaming.huskycore.registries.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

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
            if (subCommand == null) return true;
            if (commandSender.hasPermission(name + "." + subCommand.getLabel().toLowerCase())) {
                commandSender.sendMessage(getPermissionsLocale().prefix());
                return true;
            }
            subCommand.run(commandSender, strings);

        } else {
            run(commandSender, strings);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) return commandRegistry.getSubCommands().stream().map(c -> c.getLabel().toLowerCase()).collect(Collectors.toList());
        if( strings.length > 1) {
            SubCommand subCommand = commandRegistry.getSubCommand(strings[0].toLowerCase());
            if (subCommand != null) return subCommand.onTabComplete(strings);
        }
        return List.of();
    }

    public String getName() {
        return name;
    }

    public abstract Parseable getPermissionsLocale();

    public abstract void run(CommandSender commandSender, String[] strings);
}