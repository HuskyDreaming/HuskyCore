package com.huskydreaming.huskycore.inventories.modules;

import com.huskydreaming.huskycore.inventories.InventoryItem;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryModuleProvider implements InventoryProvider {

    protected int rows;
    protected final List<InventoryModule> modules = new ArrayList<>();

    public void deserialize(Player player, InventoryModule... modules) {
        int size = 0;
        for (InventoryModule module : modules) {
            if (module.isValid(player)) continue;
            this.modules.add(module);

            size += 1;
        }

        rows = (int) Math.ceil((double) size / 9);
    }

    public void deserialize(Player player, List<InventoryModule> modules) {
        int size = 0;
        for (InventoryModule module : modules) {
            if (module.isValid(player)) continue;
            this.modules.add(module);

            size += 1;
        }

        rows = (int) Math.ceil((double) size / 9);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());

        List<ClickableItem> clickableItems = new ArrayList<>();
        for(InventoryModule module : modules) {
            ItemStack itemStack = module.itemStack(player);

            clickableItems.add(ClickableItem.of(itemStack, e -> module.run(e, contents)));
        }

        Pagination pagination = contents.pagination();
        pagination.setItems(clickableItems.toArray(new ClickableItem[0]));
        pagination.setItemsPerPage(Math.min(rows * 9, 3 * 9));
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));

        if (!pagination.isLast() && !pagination.isFirst()) {
            contents.set(4, 1, InventoryItem.previous(player, contents));
            contents.set(4, 7, InventoryItem.next(player, contents));
        }

        if (!pagination.isLast()) {
            contents.set(4, 7, InventoryItem.next(player, contents));
        }

        if (!pagination.isFirst()) {
            contents.set(4, 1, InventoryItem.previous(player, contents));
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    public int getRows() {
        return rows;
    }
}