package com.huskydreaming.huskycore.registries;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.CommandExecutor;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.huskycore.interfaces.Registry;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class CommandRegistry implements Registry {

    private CommandExecutor commandExecutor;
    private final Set<SubCommand> subCommands = new HashSet<>();

    @Override
    public void deserialize(HuskyPlugin plugin) {
        try {
            Server server = Bukkit.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(server);
            commandMap.register(commandExecutor.getName(), commandExecutor);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public SubCommand getSubCommand(String string) {
        return subCommands.stream().filter(s ->
                Stream.of(s.getAliases()).map(String::toLowerCase).toList().contains(string) &&
                        s.getLabel().equalsIgnoreCase(string))
                .findFirst()
                .orElse(null);
    }

    public void add(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public Set<SubCommand> getSubCommands() {
        return Collections.unmodifiableSet(subCommands);
    }
}
