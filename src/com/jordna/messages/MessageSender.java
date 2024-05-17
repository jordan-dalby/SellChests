package com.jordna.messages;

import com.jordna.main.SellChests;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageSender
{

    private final SellChests instance;

    public MessageSender(SellChests instance)
    {
        this.instance = instance;
    }

    private String getMessage(Severity severity, String message)
    {
        String format = instance.getSellChestConfigManager().getMessageFormat(severity);
        return ChatColor.translateAlternateColorCodes('&', format.replace("{message}", message));
    }

    public void sendMessage(CommandSender sender, Severity severity, String message)
    {
        if (sender instanceof Player player)
            sendMessage(player, severity, message);
        else
            sendMessage(severity, message);
    }

    public void sendMessage(OfflinePlayer player, Severity severity, String message)
    {
        if (player == null || !player.isOnline())
        {
            sendMessage(severity, message);
            return;
        }
        sendMessage((Player)player, severity, message);
    }

    public void sendMessage(Player player, Severity severity, String message)
    {
        String formattedMessage = getMessage(severity, message);

        if (player == null)
        {
            System.out.println(formattedMessage);
            return;
        }

        if (player.isOnline())
        {
            player.sendMessage(formattedMessage);
        }
    }

    public void sendMessage(Severity severity, String message)
    {
        String formattedMessage = getMessage(severity, message);
        System.out.println(formattedMessage);
    }

}
