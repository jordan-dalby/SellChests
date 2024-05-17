package com.jordna.commands;

import com.jordna.main.SellChests;
import com.jordna.messages.Severity;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public class GiveSellChestItemCommand implements CommandExecutor
{

    private final SellChests instance;
    public GiveSellChestItemCommand(SellChests instance)
    {
        this.instance = instance;
        Objects.requireNonNull(instance.getCommand("scgive")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String cmd, @NonNull String[] args)
    {
        if (command.getLabel().equalsIgnoreCase("scgive"))
        {
            if (sender instanceof Player player)
            {
                if (!player.hasPermission("sellchests.give"))
                {
                    instance.getMessageSender().sendMessage(player, Severity.ERROR, "You don't have permission to use this command!");
                    return true;
                }
            }
            if (args.length >= 3)
            {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null)
                {
                    instance.getMessageSender().sendMessage(sender, Severity.ERROR, "Couldn't find player " + args[0]);
                    return true;
                }

                try
                {
                    int uses = Integer.parseInt(args[1]);
                    int multiplier = Integer.parseInt(args[2]);
                    player.getInventory().addItem(instance.getSellChestItemManager().getSellChestItem(uses, multiplier));
                    instance.getMessageSender().sendMessage(sender, Severity.INFO, "Item given");
                }
                catch (NumberFormatException e)
                {
                    instance.getMessageSender().sendMessage(sender, Severity.ERROR, "Invalid number passed as uses or multiplier value");
                    return true;
                }
            }
            else
            {
                instance.getMessageSender().sendMessage(sender, Severity.ERROR, "Invalid command format, /scgive [player name] [uses] [multiplier]");
                return true;
            }
        }
        return true;
    }
}
