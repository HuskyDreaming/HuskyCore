package com.huskydreaming.huskycore.handlers.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.abstraction.AbstractCommand;
import com.huskydreaming.huskycore.handlers.interfaces.CommandHandler;
import org.bukkit.command.PluginCommand;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CommandHandlerImpl implements CommandHandler {

    private AbstractCommand abstractCommand;
    private final Set<Command> commands = new HashSet<>();

    @Override
    public void initialize(HuskyPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand(abstractCommand.getLabel());
        if(pluginCommand != null) pluginCommand.setExecutor(abstractCommand);
    }

    @Override
    public void setCommandExecutor(AbstractCommand abstractCommand) {
        this.abstractCommand = abstractCommand;
    }

    @Override
    public AbstractCommand getAbstractCommand() {
        return abstractCommand;
    }

    @Override
    public Command getCommand(String string) {
        return commands.stream().filter(s -> s.getLabel().equalsIgnoreCase(string))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void add(Command command) {
        commands.add(command);
    }

    @Override
    public void remove(String name) {
        commands.removeIf(subCommand -> subCommand.getLabel().equalsIgnoreCase(name));
    }

    @Override
    public Set<Command> getCommands() {
        return Collections.unmodifiableSet(commands);
    }
}
