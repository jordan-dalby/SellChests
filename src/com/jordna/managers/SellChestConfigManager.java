package com.jordna.managers;

import com.jordna.main.SellChests;
import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SellChestConfigManager
{

    private final SellChests instance;

    public SellChestConfigManager(SellChests instance)
    {
        this.instance = instance;
        this.initialize();
    }

    private void initialize()
    {
        instance.saveDefaultConfig();

        instance.getConfig().addDefault("item.material", "book");
        instance.getConfig().addDefault("item.name", "&9Sell Chest Talisman");
        List<String> lore = new ArrayList<>();
        lore.add("&c{uses} uses remaining");
        lore.add("&cMultiplier: {multiplier}");
        instance.getConfig().addDefault("item.lore", lore);

        instance.getConfig().options().copyDefaults(true);
        instance.saveConfig();
    }

    public Material getItem()
    {
        String itemName = instance.getConfig().getString("item.material");
        if (itemName == null)
        {
            System.out.println("ERROR: item.material is not defined in the config");
            return Material.BOOK;
        }
        return Material.getMaterial(itemName.toUpperCase());
    }

    public String getDisplayName()
    {
        String displayName = instance.getConfig().getString("item.name");
        if (displayName == null)
        {
            System.out.println("ERROR: item.displayName is not defined in the config");
        }
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(displayName));
    }

    public List<String> getLore()
    {
        if (instance.getConfig().getString("item.lore") == null)
        {
            System.out.println("ERROR: item.lore is not defined in the config");
        }
        List<String> colouredLore = new ArrayList<>();
        for (String s : instance.getConfig().getStringList("item.lore"))
            colouredLore.add(ChatColor.translateAlternateColorCodes('&', s));
        return colouredLore;
    }

}
