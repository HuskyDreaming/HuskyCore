package com.huskydreaming.huskycore.inventories;

import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface InventoryModule {

    ItemStack itemStack(Player player);

    default void run(InventoryClickEvent event, InventoryContents contents) {

    }

    default boolean isValid(Player player) {
        return false;
    }
}
