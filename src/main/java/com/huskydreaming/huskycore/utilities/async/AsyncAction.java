package com.huskydreaming.huskycore.utilities.async;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

public class AsyncAction<T> {

    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private final Plugin plugin;
    private final Supplier<T> asyncSupplier;
    private final Consumer<Throwable> asyncErrorHandler;

    private AsyncAction(Plugin plugin, Supplier<T> asyncSupplier, Consumer<Throwable> asyncErrorHandler) {
        this.asyncSupplier = asyncSupplier;
        this.plugin = plugin;
        this.asyncErrorHandler = asyncErrorHandler;
    }

    private static Consumer<Throwable> getDefaultLogger(Plugin plugin) {
        return e -> plugin.getLogger().log(Level.SEVERE, "[Database] generated an exception in an AsyncAction.", e);
    }

    public static <T> AsyncAction<T> supplyAsync(Plugin plugin, Supplier<T> asyncSupplier) {
        return new AsyncAction<>(plugin, asyncSupplier, getDefaultLogger(plugin));
    }

    public static <T> AsyncAction<T> supplyAsync(Plugin plugin, Supplier<T> asyncSupplier, Consumer<Throwable> asyncErrorHandler) {
        return new AsyncAction<>(plugin, asyncSupplier, asyncErrorHandler);
    }

    public void queue() {
        executeAsync(asyncSupplier, e -> {});
    }

    public void queue(Consumer<T> syncedConsumer, Consumer<Throwable> syncedErrorHandler) {
        executeAsync(asyncSupplier, syncedConsumer, asyncErrorHandler, syncedErrorHandler);
    }

    public void queue(Consumer<T> syncedConsumer) {
        executeAsync(asyncSupplier, syncedConsumer);
    }

    private void executeAsync(Supplier<T> asyncSupplier, Consumer<T> syncedConsumer) {
        executeAsync(asyncSupplier, syncedConsumer, asyncErrorHandler, getDefaultLogger(plugin));
    }

    private void executeAsync(Supplier<T> asyncSupplier, Consumer<T> syncedConsumer, Consumer<Throwable> asyncErrorHandler, Consumer<Throwable> syncedErrorHandler) {
        EXECUTOR.execute(() -> {
            T result;
            try {
                result = asyncSupplier.get();
            } catch (Throwable e) {
                asyncErrorHandler.accept(e);
                return;
            }
            SCHEDULER.runTask(plugin, () -> {
                try {
                    syncedConsumer.accept(result);
                } catch (Throwable e) {
                    syncedErrorHandler.accept(e);
                }
            });
        });
    }
}