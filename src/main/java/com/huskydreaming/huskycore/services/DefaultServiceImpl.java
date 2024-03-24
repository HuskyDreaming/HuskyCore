package com.huskydreaming.huskycore.services;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.parseables.DefaultMenu;
import com.huskydreaming.huskycore.storage.Yaml;

public class DefaultServiceImpl implements DefaultService {

    private Yaml menu;

    @Override
    public void deserialize(HuskyPlugin plugin) {
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
