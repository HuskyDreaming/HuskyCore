package com.huskydreaming.huskycore.inventories;

import com.huskydreaming.huskycore.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryItem {

    public static ClickableItem border() {
        return ClickableItem.empty(ItemBuilder.create()
                .setDisplayName(ChatColor.RESET + "")
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .build());
    }

    public static ClickableItem next(Player player, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName("&fNext")
                .setLore("&7Click for next page")
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, event -> contents.inventory().open(player, contents.pagination().next().getPage()));
    }

    public static ClickableItem back(Player player, SmartInventory smartInventory) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName("&fBack")
                .setLore("&7Click to go back")
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, event -> smartInventory.open(player));
    }

    public static ClickableItem previous(Player player, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName("&fPrevious")
                .setLore("&7Click for previous page")
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, e -> contents.inventory().open(player, contents.pagination().previous().getPage()));
    }
}
