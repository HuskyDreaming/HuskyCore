package com.huskydreaming.huskycore.registries;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.AbstractCommand;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.huskycore.interfaces.Registry;
import org.bukkit.command.PluginCommand;

import java.util.*;
import java.util.stream.Stream;

public class CommandRegistry implements Registry {

    private AbstractCommand abstractCommand;
    private final Set<SubCommand> subCommands = new HashSet<>();

    @Override
    public void deserialize(HuskyPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand(abstractCommand.getName());
        if(pluginCommand != null) pluginCommand.setExecutor(abstractCommand);
    }

    public void setCommandExecutor(AbstractCommand abstractCommand) {
        this.abstractCommand = abstractCommand;
    }

    public AbstractCommand getAbstractCommand() {
        return abstractCommand;
    }

    public SubCommand getSubCommand(String string) {
        return subCommands.stream().filter(s ->
                Stream.of(s.getAliases()).map(String::toLowerCase).toList().contains(string) ||
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
