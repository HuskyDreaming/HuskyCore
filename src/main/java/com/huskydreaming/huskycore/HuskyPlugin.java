package com.huskydreaming.huskycore;

import com.huskydreaming.huskycore.registries.CommandRegistry;
import com.huskydreaming.huskycore.registries.ServiceRegistry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public abstract class HuskyPlugin extends JavaPlugin {

    protected CommandRegistry commandRegistry;
    protected ServiceRegistry serviceRegistry;

    @Override
    public void onEnable() {
        commandRegistry = new CommandRegistry();
        serviceRegistry = new ServiceRegistry();
    }

    protected void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        Arrays.asList(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    public <T> T provide(Class<T> tClass) {
        return serviceRegistry.provide(tClass);
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}