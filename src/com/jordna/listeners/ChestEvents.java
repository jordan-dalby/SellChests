package com.jordna.listeners;

import com.jordna.main.SellChests;
import com.jordna.types.SellChestItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChestEvents implements Listener
{

    private final SellChests instance;
    public ChestEvents(SellChests instance)
    {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        List<InventoryAction> addActions = new ArrayList<>(Arrays.asList(
                InventoryAction.DROP_ALL_CURSOR,
                InventoryAction.DROP_ONE_CURSOR,
                InventoryAction.DROP_ALL_SLOT,
                InventoryAction.DROP_ONE_SLOT,
                InventoryAction.PLACE_ALL,
                InventoryAction.PLACE_ONE,
                InventoryAction.PLACE_SOME,
                InventoryAction.SWAP_WITH_CURSOR
        ));

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (event.getClickedInventory() == null)
                    return;

                SellChestItem sellChestItem = new SellChestItem(instance, event.getCurrentItem());
                if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
                {
                    if (Objects.requireNonNull(event.getInventory()).getType() == InventoryType.CHEST)
                    {
                        if (instance.getSellChestItemManager().getItemStackIsSellChestItem(Objects.requireNonNull(event.getCurrentItem())))
                        {
                            if (event.getWhoClicked() instanceof Player player)
                            {
                                sellChestItem.setOwner(player.getUniqueId());
                            }
                        }
                        instance.getSellChestManager().sellChestContents(event.getInventory());
                    }
                }
                else if (addActions.contains(event.getAction()))
                {
                    if (Objects.requireNonNull(event.getClickedInventory()).getType() == InventoryType.CHEST)
                    {
                        if (instance.getSellChestItemManager().getItemStackIsSellChestItem(Objects.requireNonNull(event.getCurrentItem())))
                        {
                            if (event.getWhoClicked() instanceof Player player)
                            {
                                sellChestItem.setOwner(player.getUniqueId());
                            }
                        }
                        instance.getSellChestManager().sellChestContents(event.getInventory());
                    }
                }
            }
        }.runTaskLater(instance, 2);
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event)
    {
        if (event.getDestination().getType() == InventoryType.CHEST)
        {
            instance.getSellChestManager().sellChestContents(event.getDestination());
        }
    }

}
