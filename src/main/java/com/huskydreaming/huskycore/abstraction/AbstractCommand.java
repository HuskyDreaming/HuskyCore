package com.huskydreaming.huskycore.abstraction;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.command.Command;
import com.huskydreaming.huskycore.interfaces.command.providers.CommandProvider;
import com.huskydreaming.huskycore.interfaces.command.providers.ConsoleCommandProvider;
import com.huskydreaming.huskycore.interfaces.command.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.interfaces.Parseable;
import com.huskydreaming.huskycore.registries.CommandRegistry;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandExecutor, Command, TabCompleter {

    private final CommandRegistry commandRegistry;

    public AbstractCommand(HuskyPlugin huskyPlugin) {
        this.commandRegistry = huskyPlugin.getCommandRegistry();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            Command subCommand = commandRegistry.getCommand(strings[0]);
            if (subCommand == null) {
                commandSender.sendMessage(getUsage().prefix(strings[0]));
                return true;
            }

            if(commandSender instanceof ConsoleCommandSender consoleCommandSender) {
                if(subCommand instanceof ConsoleCommandProvider consoleCommandProvider) {
                    consoleCommandProvider.onCommand(consoleCommandSender, strings);
                    return true;
                }
            }

            if(commandSender instanceof Player player) {
                if (!(player.hasPermission(getLabel() + "." + subCommand.getLabel().toLowerCase()) || player.isOp())) {
                    commandSender.sendMessage(getPermission().prefix());
                    return true;
                }

                if (subCommand instanceof PlayerCommandProvider playerCommandProvider) {
                    playerCommandProvider.onCommand(player, strings);
                    return true;
                }
            }
            if(subCommand instanceof CommandProvider commandProvider) {
                commandProvider.onCommand(commandSender, strings);
                return true;
            }
        } else {
            onCommand(commandSender, strings);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) return commandRegistry.getCommands().stream().map(c -> c.getLabel().toLowerCase()).collect(Collectors.toList());
        if(strings.length > 1) {
            Command subCommand = commandRegistry.getCommand(strings[0].toLowerCase());
            if (subCommand != null) {

                if(commandSender instanceof ConsoleCommandSender consoleCommandSender) {
                    if(subCommand instanceof ConsoleCommandProvider consoleCommandProvider) {
                        return consoleCommandProvider.onTabComplete(consoleCommandSender, strings);
                    }
                }

                if(commandSender instanceof Player player) {
                    if(subCommand instanceof PlayerCommandProvider playerCommandProvider) {
                        return playerCommandProvider.onTabComplete(player, strings);
                    }
                }

                if(subCommand instanceof CommandProvider commandProvider) {
                    return commandProvider.onTabComplete(commandSender, strings);
                }
            }
        }
        return List.of();
    }

    public abstract void onCommand(CommandSender commandSender, String[] strings);

    public abstract Parseable getUsage();
    public abstract Parseable getPermission();
}