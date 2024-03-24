package com.huskydreaming.huskycore.storage.parseables;

import com.google.common.base.Functions;
import com.huskydreaming.huskycore.interfaces.Parseable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum DefaultMenu implements Parseable {

    // General Menu Items
    DESCRIPTION("&f{0}"),
    BACK_TITLE("&fBack"),
    BACK_LORE(Collections.singletonList("&7Click to go back.")),
    PREVIOUS_TITLE("&fPrevious"),
    PREVIOUS_LORE(Collections.singletonList("&7Click for previous page.")),
    NEXT_TITLE("&fNext"),
    NEXT_LORE(Collections.singletonList("&7Click for next page.")),
    ENABLE_TITLE("&a{0}"),
    ENABLED_DESCRIPTION("&7Click to disable."),
    ENABLE_MATERIAL("LIME_DYE"),
    DISABLED_TITLE("&c{0}"),
    DISABLED_DESCRIPTION("&7Click to enable."),
    DISABLED_MATERIAL("GRAY_DYE"),
    NO_PERMISSIONS_TITLE("&c{0}"),
    NO_PERMISSIONS_LORE(Collections.singletonList("&7No permissions."));

    private final String def;
    private final List<String> list;
    private static FileConfiguration menuConfiguration;

    DefaultMenu(String def) {
        this.def = def;
        this.list = null;
    }

    DefaultMenu(List<String> list) {
        this.list = list;
        this.def = null;
    }

    @Override
    public String prefix(Object... objects) {
        return null;
    }

    @Nullable
    public String parse() {
        String message = menuConfiguration.getString(toString(), def);
        if (message == null) return null;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Nullable
    public List<String> parseList() {
        List<?> objects = menuConfiguration.getList(toString(), list);
        if (objects == null) return null;
        return objects.stream().map(Functions.toStringFunction()).collect(Collectors.toList());
    }

    @NotNull
    public String toString() {
        return name().toLowerCase().replace("_", ".");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        DefaultMenu.menuConfiguration = configuration;
    }
}
