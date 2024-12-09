package com.huskydreaming.huskycore.utilities.builders;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {

    private boolean enchanted = false;
    private String displayName = "Item";
    private List<String> lore = null;
    private Material material = Material.STONE;
    private Color potionColor;
    private int amount = 1;

    public static ItemBuilder create() {
        return new ItemBuilder();
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore == null) {
            this.lore = new ArrayList<>();
            return this;
        }
        this.lore = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
        return this;
    }


    public ItemBuilder setLore(String... strings) {
        List<String> lore = List.of(strings);
        this.lore = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
        return this;
    }

    public ItemBuilder setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setPotionColor(Color potionColor) {
        this.potionColor = potionColor;
        return this;
    }

    public ItemStack buildPlayer(OfflinePlayer offlinePlayer) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (skullMeta != null) {
            if (enchanted) skullMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            skullMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            skullMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            skullMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            skullMeta.addItemFlags(ItemFlag.HIDE_DYE);
            skullMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            skullMeta.setDisplayName(displayName);
            skullMeta.setOwnerProfile(offlinePlayer.getPlayerProfile());
            if (lore != null) skullMeta.setLore(lore);
            itemStack.setItemMeta(skullMeta);
        }
        return itemStack;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (enchanted) itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setDisplayName(displayName);
            if (lore != null) itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }
        itemStack.setAmount(amount);
        return itemStack;
    }

    public ItemStack buildPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        if (potionMeta != null) {
            potionMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            potionMeta.setDisplayName(displayName);
            if(potionColor != null) potionMeta.setColor(potionColor);
            if (lore != null) potionMeta.setLore(lore);
            itemStack.setItemMeta(potionMeta);
        }
        itemStack.setAmount(amount);
        return itemStack;
    }
}