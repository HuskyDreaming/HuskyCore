package com.huskydreaming.huskycore.handlers.implementations;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.handlers.interfaces.DefaultConfigHandler;
import com.huskydreaming.huskycore.storage.parseables.DefaultMenu;
import com.huskydreaming.huskycore.storage.Yaml;

public class DefaultConfigHandlerImpl implements DefaultConfigHandler {

    private Yaml menu;

    @Override
    public void initialize(HuskyPlugin plugin) {
        // Localization for menus
        menu = new Yaml("menus/general");
        menu.load(plugin);
        DefaultMenu.setConfiguration(menu.getConfiguration());

        for (DefaultMenu message : DefaultMenu.values()) {
            menu.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }
        menu.save();
    }

    @Override
    public Yaml getMenu() {
        return menu;
    }
}