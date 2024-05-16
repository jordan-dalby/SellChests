package com.jordna.commands;

import com.jordna.main.SellChests;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                    sendMessage(sender, ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }
            }
            if (args.length >= 3)
            {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null)
                {
                    sendMessage(sender, ChatColor.RED + "Couldn't find player " + args[0]);
                    return true;
                }

                try
                {
                    int uses = Integer.parseInt(args[1]);
                    int multiplier = Integer.parseInt(args[2]);
                    player.getInventory().addItem(instance.getSellChestItemManager().getSellChestItem(uses, multiplier));
                    sendMessage(sender, ChatColor.BLUE + "Item given");
                }
                catch (NumberFormatException e)
                {
                    sendMessage(sender, ChatColor.RED + "Invalid number passed as uses or multiplier value");
                    return true;
                }
            }
            else
            {
                sendMessage(sender, ChatColor.RED + "Invalid command format, /scgive [player name] [uses] [multiplier]");
                return true;
            }
        }
        return true;
    }

    private void sendMessage(CommandSender sender, String message)
    {
        if (sender instanceof Player player)
            player.sendMessage(message);
        else
            System.out.println(message);
    }
}
