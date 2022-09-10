package nl.corwindev.streamervschat.command;

import nl.corwindev.streamervschat.main;
import nl.corwindev.streamervschat.youtube.YouTubeConnectionHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class YouTubeReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!main.plugin.getConfig().getBoolean("youtube.enabled")){
            commandSender.sendMessage(ChatColor.RED + "YouTube is not enabled.");
            return true;
        }
        YouTubeConnectionHelper.reload();
        commandSender.sendMessage(ChatColor.GREEN + "Reloaded YouTube connection.");
        return true;
    }
}