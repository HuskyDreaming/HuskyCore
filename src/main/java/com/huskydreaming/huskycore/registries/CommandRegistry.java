package com.huskydreaming.huskycore.registries;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.abstraction.AbstractCommand;
import com.huskydreaming.huskycore.interfaces.command.Command;
import com.huskydreaming.huskycore.interfaces.Registry;
import org.bukkit.command.PluginCommand;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CommandRegistry implements Registry {

    private AbstractCommand abstractCommand;
    private final Set<Command> commands = new HashSet<>();

    @Override
    public void deserialize(HuskyPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand(abstractCommand.getLabel());
        if(pluginCommand != null) pluginCommand.setExecutor(abstractCommand);
    }

    public void setCommandExecutor(AbstractCommand abstractCommand) {
        this.abstractCommand = abstractCommand;
    }

    public AbstractCommand getAbstractCommand() {
        return abstractCommand;
    }

    public Command getCommand(String string) {
        return commands.stream().filter(s -> s.getLabel().equalsIgnoreCase(string))
                .findFirst()
                .orElse(null);
    }

    public void add(Command command) {
        commands.add(command);
    }

    public void remove(String name) {
        commands.removeIf(subCommand -> subCommand.getLabel().equalsIgnoreCase(name));
    }

    public Set<Command> getCommands() {
        return Collections.unmodifiableSet(commands);
    }
}
