package com.huskydreaming.huskycore.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public abstract class InventoryPageProvider<E> implements InventoryProvider {

    protected final int rows;

    protected boolean updates;
    protected E[] array;

    public InventoryPageProvider(int rows) {
        this.rows = rows;
    }

    public InventoryPageProvider(int rows, E[] array) {
        this.rows = rows;
        this.array = array;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());

        setupItems(player, contents);
        setupPagination(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        if(updates) {
            int state = contents.property("state", 0);
            contents.setProperty("state", state + 1);

            if (state % 20 != 0) return;
            setupItems(player, contents);
        }
    }

    public abstract ItemStack construct(Player player, int index, E e);

    public abstract void run(InventoryClickEvent event, E e, InventoryContents contents);

    public void setArray(E[] array) {
        this.array = array;
    }

    private void setupPagination(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
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

        setupItems(player, contents);
    }

    public void setupItems(Player player, InventoryContents contents) {
        ClickableItem[] clickableItems = new ClickableItem[array.length];
        for (int i = 0; i < clickableItems.length; i++) {
            AtomicInteger atomicInteger = new AtomicInteger(i);
            ItemStack itemStack = construct(player, i + 1, array[atomicInteger.get()]);
            Consumer<InventoryClickEvent> consumer = e -> run(e, array[atomicInteger.get()], contents);
            clickableItems[i] = ClickableItem.of(itemStack, consumer);
        }
        contents.pagination().setItems(clickableItems);
    }
}