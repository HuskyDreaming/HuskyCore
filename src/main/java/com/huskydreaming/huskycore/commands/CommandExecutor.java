package com.huskydreaming.huskycore.commands;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Parseable;
import com.huskydreaming.huskycore.registries.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandExecutor extends Command implements TabCompleter {
    private final CommandRegistry commandRegistry;

    public CommandExecutor(String name, HuskyPlugin huskyPlugin) {
        super(name);
        this.commandRegistry = huskyPlugin.getCommandRegistry();
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            SubCommand subCommand = commandRegistry.getSubCommand(strings[0]);
            if (subCommand == null) return true;
            if (commandSender.hasPermission(getName() + "." + subCommand.getLabel().toLowerCase())) {
                commandSender.sendMessage(getPermissionsLocale().parameterize());
                return true;
            }
            subCommand.run(commandSender, strings);

        } else {
            run(commandSender, strings);
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1)
            return commandRegistry.getSubCommands().stream().map(c -> c.getLabel().toLowerCase()).collect(Collectors.toList());
        if (args.length > 1) {
            SubCommand subCommand = commandRegistry.getSubCommand(args[0]);
            if (subCommand != null) return subCommand.onTabComplete(args);
        }
        return List.of();
    }

    public abstract Parseable getPermissionsLocale();

    public abstract void run(CommandSender commandSender, String[] strings);
}