package nl.corwindev.streamervschat.command;

import nl.corwindev.streamervschat.discord.DiscordConnectionHelper;
import nl.corwindev.streamervschat.main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DiscordReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!main.plugin.getConfig().getBoolean("discord.enabled")){
            commandSender.sendMessage(ChatColor.RED + "Discord is not enabled.");
            return true;
        }
        DiscordConnectionHelper.reload();
        commandSender.sendMessage(ChatColor.GREEN + "Reloaded Discord connection.");
        return true;
    }
}