package com.jordna.types;

import com.jordna.main.SellChests;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SellChestItem
{

    private final ItemStack stack;
    private final SellChests instance;

    public SellChestItem(SellChests instance, ItemStack stack)
    {
        this.instance = instance;
        this.stack = stack;
    }

    public int getRemainingUses()
    {
        if (!stack.hasItemMeta())
            return -1;

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(meta).getPersistentDataContainer();

        if(container.has(instance.remainingUsesKey, PersistentDataType.INTEGER))
        {
            Integer remainingUses = container.get(instance.remainingUsesKey, PersistentDataType.INTEGER);
            return remainingUses != null ? remainingUses : 0;
        }
        return 0;
    }

    public void setRemainingUses(int value)
    {
        if (!stack.hasItemMeta())
            return;

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(meta).getPersistentDataContainer();

        container.set(instance.remainingUsesKey, PersistentDataType.INTEGER, value);

        List<String> lore = getLore(value, getMultiplier());
        meta.setLore(lore);

        stack.setItemMeta(meta);
    }

    public void reduceRemainingUses()
    {
        int currentUses = getRemainingUses();
        setRemainingUses(currentUses - 1);
    }

    public int getMultiplier()
    {
        if (!stack.hasItemMeta())
            return -1;

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(meta).getPersistentDataContainer();

        if(container.has(instance.multiplier, PersistentDataType.INTEGER))
        {
            Integer itemMultiplier = container.get(instance.multiplier, PersistentDataType.INTEGER);
            return itemMultiplier != null ? itemMultiplier : 1;
        }
        return 1;
    }

    public UUID getOwner()
    {
        if (!stack.hasItemMeta())
            return null;

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(meta).getPersistentDataContainer();

        if(container.has(instance.owner, PersistentDataType.STRING))
        {
            String itemOwner = container.get(instance.owner, PersistentDataType.STRING);
            return itemOwner != null ? UUID.fromString(itemOwner) : null;
        }
        return null;
    }

    public void setOwner(UUID uuid)
    {
        if (!stack.hasItemMeta())
            return;

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(meta).getPersistentDataContainer();

        container.set(instance.owner, PersistentDataType.STRING, uuid.toString());
        stack.setItemMeta(meta);
    }

    public OfflinePlayer getPlayerOwner()
    {
        return Bukkit.getOfflinePlayer(getOwner());
    }

    public List<String> getLore(int uses, int multiplier)
    {
        List<String> lore = instance.getSellChestConfigManager().getLore();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            String stringUses = uses == -1 ? "Unlimited" : String.valueOf(uses);
            line = line.replace("{uses}", stringUses);
            line = line.replace("{multiplier}", String.valueOf(multiplier));
            lore.set(i, line);
        }
        return lore;
    }

}
