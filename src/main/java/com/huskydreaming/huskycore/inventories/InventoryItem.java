package com.huskydreaming.huskycore.inventories;

import com.huskydreaming.huskycore.storage.parseables.DefaultMenu;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class InventoryItem {

    public static ItemStack of(ChatColor chatColor, String filter) {
        return ItemBuilder.create()
                .setDisplayName(DefaultMenu.FILTER_TITLE.parameterize(chatColor, filter))
                .setMaterial(Material.HOPPER_MINECART)
                .setLore( DefaultMenu.FILTER_LORE.parseList())
                .build();
    }

    public static ItemStack of(boolean active, String name, String description) {
        return of(active, true, name, description);
    }

    public static ItemStack of(boolean active, boolean editable, String name, String description) {
        DefaultMenu material = active ? DefaultMenu.ENABLE_MATERIAL : DefaultMenu.DISABLED_MATERIAL;
        DefaultMenu title = active ? DefaultMenu.ENABLE_TITLE : DefaultMenu.DISABLED_TITLE;
        DefaultMenu edit = active ? DefaultMenu.ENABLED_DESCRIPTION_EDITABLE : DefaultMenu.DISABLED_DESCRIPTION_EDITABLE;
        DefaultMenu lore = active ? DefaultMenu.ENABLED_DESCRIPTION : DefaultMenu.DISABLED_DESCRIPTION;
        DefaultMenu finalLore  =  editable ? edit : lore;

        return ItemBuilder.create()
                .setDisplayName(title.parameterize(name))
                .setMaterial(Material.valueOf(material.parse()))
                .setLore(finalLore.parameterizeList(description))
                .build();
    }

    public static ItemStack of(boolean active, Material material, String name, String description) {
        return of(active, true, material, name, description);
    }

    public static ItemStack of(boolean active, boolean editable, Material material, String name, String description) {
        DefaultMenu title = active ? DefaultMenu.ENABLE_TITLE : DefaultMenu.DISABLED_TITLE;
        DefaultMenu edit = active ? DefaultMenu.ENABLED_DESCRIPTION_EDITABLE : DefaultMenu.DISABLED_DESCRIPTION_EDITABLE;
        DefaultMenu lore = active ? DefaultMenu.ENABLED_DESCRIPTION : DefaultMenu.DISABLED_DESCRIPTION;
        DefaultMenu finalLore  =  editable ? edit : lore;

        return ItemBuilder.create()
                .setDisplayName(title.parameterize(name))
                .setMaterial(material)
                .setLore(finalLore.parameterizeList(description))
                .build();
    }

    public static ClickableItem of(boolean permission, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        if (permission) return ClickableItem.of(itemStack, consumer);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            String displayName = ChatColor.stripColor(itemMeta.getDisplayName());
            return ClickableItem.empty(ItemBuilder.create()
                    .setDisplayName(DefaultMenu.NO_PERMISSIONS_TITLE.parameterize(displayName))
                    .setLore(DefaultMenu.NO_PERMISSIONS_LORE.parseList())
                    .setMaterial(Material.GRAY_DYE)
                    .build());
        }
        return ClickableItem.empty(new ItemStack(Material.GRAY_DYE));
    }

    public static ItemStack of(boolean permission, ItemStack itemStack) {
        if (permission) return itemStack;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            String displayName = ChatColor.stripColor(itemMeta.getDisplayName());
            return ItemBuilder.create()
                    .setDisplayName(DefaultMenu.NO_PERMISSIONS_TITLE.parameterize(displayName))
                    .setLore(DefaultMenu.NO_PERMISSIONS_LORE.parseList())
                    .setMaterial(Material.GRAY_DYE)
                    .build();
        }
        return new ItemStack(Material.GRAY_DYE);
    }

    public static ClickableItem border() {
        return ClickableItem.empty(ItemBuilder.create()
                .setDisplayName(ChatColor.RESET + "")
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .build());
    }

    public static ClickableItem next(Player player, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(DefaultMenu.NEXT_TITLE.parse())
                .setLore(DefaultMenu.NEXT_LORE.parseList())
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, event -> contents.inventory().open(player, contents.pagination().next().getPage()));
    }

    public static ClickableItem back(Player player, SmartInventory smartInventory) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(DefaultMenu.BACK_TITLE.parse())
                .setLore(DefaultMenu.BACK_LORE.parseList())
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, event -> smartInventory.open(player));
    }

    public static ClickableItem previous(Player player, InventoryContents contents) {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(DefaultMenu.PREVIOUS_TITLE.parse())
                .setLore(DefaultMenu.PREVIOUS_LORE.parseList())
                .setMaterial(Material.ARROW)
                .build();

        return ClickableItem.of(itemStack, e -> contents.inventory().open(player, contents.pagination().previous().getPage()));
    }
}