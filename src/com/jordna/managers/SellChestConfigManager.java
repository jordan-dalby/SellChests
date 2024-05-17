package com.jordna.managers;

import com.jordna.main.SellChests;
import com.jordna.messages.Severity;
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

        instance.getConfig().addDefault("messages.format.info", "&b&l[INFO] &r&7{message}");
        instance.getConfig().addDefault("messages.format.debug", "&5&l[DEBUG] &r&7{message}");
        instance.getConfig().addDefault("messages.format.error", "&c&l[ERROR] &r&c{message}");

        instance.getConfig().addDefault("item.material", "book");
        instance.getConfig().addDefault("item.name", "&9Sell Chest Talisman");
        List<String> lore = new ArrayList<>();
        lore.add("&c{uses} uses remaining");
        lore.add("&cMultiplier: {multiplier}");
        instance.getConfig().addDefault("item.lore", lore);

        instance.getConfig().options().copyDefaults(true);
        instance.saveConfig();
    }

    public String getMessageFormat(Severity severity)
    {
        return instance.getConfig().getString("messages.format." + severity.name);
    }

    public Material getItem()
    {
        String itemName = instance.getConfig().getString("item.material");
        if (itemName == null)
        {
            instance.getMessageSender().sendMessage(Severity.ERROR, "item.material is not defined in the config");
            return Material.BOOK;
        }

        Material itemMaterial = Material.matchMaterial(itemName.toUpperCase());
        if (itemMaterial == null)
        {
            instance.getMessageSender().sendMessage(Severity.ERROR, "item.material value could not be mapped to a material: " + itemName);
            return Material.BOOK;
        }
        return itemMaterial;
    }

    public String getDisplayName()
    {
        String displayName = instance.getConfig().getString("item.name");
        if (displayName == null)
        {
            instance.getMessageSender().sendMessage(Severity.ERROR, "item.displayName is not defined in the config");
            return "Sell Chest Item";
        }
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(displayName));
    }

    public List<String> getLore()
    {
        if (instance.getConfig().getString("item.lore") == null)
        {
            instance.getMessageSender().sendMessage(Severity.ERROR, "item.lore is not defined in the config");
            return new ArrayList<>();
        }
        List<String> colouredLore = new ArrayList<>();
        for (String s : instance.getConfig().getStringList("item.lore"))
            colouredLore.add(ChatColor.translateAlternateColorCodes('&', s));
        return colouredLore;
    }

}
