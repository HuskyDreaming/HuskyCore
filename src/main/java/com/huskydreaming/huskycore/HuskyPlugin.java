package com.huskydreaming.huskycore;

import com.huskydreaming.huskycore.registries.CommandRegistry;
import com.huskydreaming.huskycore.registries.ServiceRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class HuskyPlugin extends JavaPlugin {

    private CommandRegistry commandRegistry;
    private ServiceRegistry serviceRegistry;

    @Override
    public void onEnable() {
        commandRegistry = new CommandRegistry();
        serviceRegistry = new ServiceRegistry();
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }
}