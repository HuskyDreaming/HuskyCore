package com.huskydreaming.huskycore.handlers.interfaces;

import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.abstraction.AbstractCommand;

import java.util.Set;

public interface CommandHandler extends Handler {
    void setCommandExecutor(AbstractCommand abstractCommand);

    AbstractCommand getAbstractCommand();

    Command getCommand(String string);

    void add(Command command);

    void remove(String name);

    Set<Command> getCommands();
}
