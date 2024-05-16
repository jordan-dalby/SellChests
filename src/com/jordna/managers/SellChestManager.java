package com.jordna.managers;

import com.jordna.main.SellChests;
import com.jordna.types.SellChestItem;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.swing.text.NumberFormatter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class SellChestManager
{

    private final SellChests instance;

    private final NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

    public SellChestManager(SellChests instance)
    {
        this.instance = instance;
    }

    public void sellChestContents(Inventory inventory)
    {
        if (inventory.firstEmpty() != -1)
            return;

        SellChestItem sellChestItem = null;
        for (int i = 0; i < inventory.getContents().length; i++)
        {
            ItemStack stack = inventory.getItem(i);
            if (stack == null)
                continue;
            if (instance.getSellChestItemManager().getItemStackIsSellChestItem(stack))
            {
                sellChestItem = instance.getSellChestItemManager().parseSellChestItem(stack);
                if (sellChestItem.getRemainingUses() > 0 || sellChestItem.getRemainingUses() == -1)
                {
                    sellChestItem.reduceRemainingUses();
                    break;
                }
            }
        }
        if (sellChestItem == null)
        {
            return;
        }

        double total = 0;
        for (int i = 0; i < inventory.getContents().length; i++)
        {
            ItemStack item = inventory.getItem(i);
            if (item == null)
                continue;

            if (instance.getSellChestItemManager().getItemStackIsSellChestItem(item))
                continue;

            BigDecimal price = instance.getEssentials().getWorth().getPrice(instance.getEssentials(), item);
            if (price == null)
                continue;

            HashMap<Integer, ItemStack> remove = inventory.removeItem(item);
            if (remove.isEmpty())
            {
                double priceDouble = price.doubleValue();
                double multipliedPrice = priceDouble * sellChestItem.getMultiplier() * item.getAmount();
                EconomyResponse response = instance.getEconomy().depositPlayer(sellChestItem.getPlayerOwner(), multipliedPrice);
                if (!response.transactionSuccess())
                {
                    // re-add the item if the transaction fails
                    inventory.addItem(item);
                }
                total += multipliedPrice;
            }
        }
        if (total != 0)
        {
            if (sellChestItem.getPlayerOwner().isOnline())
            {
                ((Player)sellChestItem.getPlayerOwner()).sendMessage(ChatColor.BLUE + "Your chest was full and auto-sold for " + formatter.format(total));
            }
        }
    }

}
