package com.jordna.managers;

import com.jordna.main.SellChests;
import com.jordna.types.SellChestItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SellChestItemManager
{

    private final SellChests instance;

    public SellChestItemManager(SellChests instance)
    {
        this.instance = instance;
    }

    public ItemStack getSellChestItem(int itemUses, int itemMultiplier)
    {
        Material selected = instance.getSellChestConfigManager().getItem();
        Material material = selected == null ? Material.BOOK : selected;

        ItemStack sellChestItem = new ItemStack(material, 1);
        ItemMeta meta = sellChestItem.getItemMeta();
        if (meta != null)
        {
            // set the NBT values
            meta.getPersistentDataContainer().set(instance.remainingUsesKey, PersistentDataType.INTEGER, itemUses);
            meta.getPersistentDataContainer().set(instance.multiplier, PersistentDataType.INTEGER, itemMultiplier);
            meta.getPersistentDataContainer().set(instance.uuid, PersistentDataType.STRING, UUID.randomUUID().toString());

            // display name
            meta.setDisplayName(instance.getSellChestConfigManager().getDisplayName());

            // lore
            List<String> lore = new SellChestItem(instance, sellChestItem).getLore(itemUses, itemMultiplier);
            meta.setLore(lore);
        }
        sellChestItem.setItemMeta(meta);

        return sellChestItem;
    }

    public boolean getItemStackIsSellChestItem(ItemStack stack)
    {
        if (stack == null)
            return false;
        if (!stack.hasItemMeta())
            return false;

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = Objects.requireNonNull(meta).getPersistentDataContainer();

        return container.has(instance.remainingUsesKey, PersistentDataType.INTEGER);
    }

    public SellChestItem parseSellChestItem(ItemStack stack)
    {
        return new SellChestItem(instance, stack);
    }

}
