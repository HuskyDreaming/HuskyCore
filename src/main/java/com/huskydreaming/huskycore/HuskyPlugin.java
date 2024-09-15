package com.huskydreaming.huskycore;

import com.huskydreaming.huskycore.implementations.DefaultServiceImpl;
import com.huskydreaming.huskycore.interfaces.DefaultService;
import com.huskydreaming.huskycore.registries.CommandRegistry;
import com.huskydreaming.huskycore.registries.ServiceRegistry;
import com.huskydreaming.huskycore.utilities.ThreadSync;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class HuskyPlugin extends JavaPlugin {

    protected CommandRegistry commandRegistry;
    protected ServiceRegistry serviceRegistry;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onEnable() {
        commandRegistry = new CommandRegistry();
        serviceRegistry = new ServiceRegistry();
        serviceRegistry.register(DefaultService.class, new DefaultServiceImpl());
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

    public void runAsync(Runnable runnable) {
        this.executorService.execute(() -> {
            try {
                runnable.run();
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        });
    }

    public void runAndWait(Runnable runnable) {
        ThreadSync threadSync = new ThreadSync();
        this.executorService.execute(() -> {
            try {
                runnable.run();
                threadSync.release();
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        });
        threadSync.waitForRelease();
    }
}