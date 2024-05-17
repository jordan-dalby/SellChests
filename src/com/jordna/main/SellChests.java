package com.jordna.main;

import com.earth2me.essentials.Essentials;
import com.jordna.commands.GiveSellChestItemCommand;
import com.jordna.listeners.ChestEvents;
import com.jordna.managers.SellChestConfigManager;
import com.jordna.managers.SellChestItemManager;
import com.jordna.managers.SellChestManager;
import com.jordna.messages.MessageSender;
import com.jordna.messages.Severity;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SellChests extends JavaPlugin
{

    private Essentials essentials;
    private Economy economy;

    private SellChestManager sellChestManager;
    private MessageSender messageSender;
    private SellChestConfigManager sellChestConfigManager;
    private SellChestItemManager sellChestItemManager;

    public final NamespacedKey remainingUsesKey = new NamespacedKey(this, "sell-chests-remaining-uses");
    public final NamespacedKey multiplier = new NamespacedKey(this, "sell-chests-multiplier");
    public final NamespacedKey owner = new NamespacedKey(this, "sell-chests-owner");
    public final NamespacedKey uuid = new NamespacedKey(this, "sell-chests-uuid"); // stop items from stacking

    @Override
    public void onEnable()
    {
        sellChestConfigManager = new SellChestConfigManager(this);
        messageSender = new MessageSender(this);
        sellChestItemManager = new SellChestItemManager(this);
        sellChestManager = new SellChestManager(this);

        new ChestEvents(this);
        new GiveSellChestItemCommand(this);

        if (!loadEssentials() || !loadVault())
        {
            messageSender.sendMessage(Severity.ERROR, "Some dependencies were not found or failed to load! Disabling SellChests.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        messageSender.sendMessage(Severity.INFO, "SellChests successfully loaded.");
    }

    private boolean loadEssentials()
    {
        if (!getServer().getPluginManager().isPluginEnabled("Essentials"))
        {
            messageSender.sendMessage(Severity.ERROR, "EssentialsX not found.");
            return false;
        }

        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        return true;
    }

    private boolean loadVault()
    {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            messageSender.sendMessage(Severity.ERROR, "Vault not found.");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            messageSender.sendMessage(Severity.ERROR, "No response from economy, Vault integration failed!");
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public Essentials getEssentials()
    {
        return essentials;
    }

    public Economy getEconomy()
    {
        return economy;
    }

    public SellChestManager getSellChestManager()
    {
        return sellChestManager;
    }

    public SellChestConfigManager getSellChestConfigManager()
    {
        return sellChestConfigManager;
    }

    public MessageSender getMessageSender()
    {
        return messageSender;
    }

    public SellChestItemManager getSellChestItemManager()
    {
        return sellChestItemManager;
    }
}
