package com.huskydreaming.huskycore;

import com.huskydreaming.huskycore.handlers.implementations.CommandHandlerImpl;
import com.huskydreaming.huskycore.handlers.interfaces.CommandHandler;
import com.huskydreaming.huskycore.handlers.interfaces.Handler;
import com.huskydreaming.huskycore.registries.Registrable;
import com.huskydreaming.huskycore.registries.Registry;
import com.huskydreaming.huskycore.registries.RegistryImpl;
import com.huskydreaming.huskycore.registries.RegistryType;
import com.huskydreaming.huskycore.repositories.Repository;
import com.huskydreaming.huskycore.services.Service;
import com.huskydreaming.huskycore.utilities.async.ThreadSync;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class HuskyPlugin extends JavaPlugin {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    protected Registry<Service> serviceRegistry;
    protected Registry<Handler> handlerRegistry;
    protected Registry<Repository> repositoryRegistry;

    public abstract void onInitialize();
    public abstract void onPostInitialize();

    @Override
    public void onLoad() {
        handlerRegistry = new RegistryImpl<>();
        handlerRegistry.register(CommandHandler.class, new CommandHandlerImpl());

        repositoryRegistry = new RegistryImpl<>();
        serviceRegistry = new RegistryImpl<>();

        onInitialize();

        repositoryRegistry.start(this, RegistryType.STARTUP);
        handlerRegistry.start(this, RegistryType.STARTUP);
        serviceRegistry.start(this, RegistryType.STARTUP);
        getLogger().info("[Registry] Successfully started all registries");
    }

    @Override
    public void onEnable() {
        onPostInitialize();

        repositoryRegistry.start(this, RegistryType.POST);
        handlerRegistry.start(this, RegistryType.POST);
        serviceRegistry.start(this, RegistryType.POST);
        getLogger().info("[Registry] Successfully ran post startup for all registries");
    }

    @Override
    public void onDisable() {
        handlerRegistry.shutdown(this);
        serviceRegistry.shutdown(this);
        repositoryRegistry.shutdown(this);
        getLogger().info("[Registry] Successfully shutdown all registries");
    }

    protected void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        Arrays.asList(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    public <K extends Registrable> K provide(Class<K> c) {
        if(Service.class.isAssignableFrom(c)) {
            return serviceRegistry.provide(c);
        } else if(Handler.class.isAssignableFrom(c)) {
            return handlerRegistry.provide(c);
        } else if(Repository.class.isAssignableFrom(c)) {
            return repositoryRegistry.provide(c);
        }

        getLogger().severe("[Registry] Could not retrieve " + c.getSimpleName() + " as it is not available");

        return null;
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