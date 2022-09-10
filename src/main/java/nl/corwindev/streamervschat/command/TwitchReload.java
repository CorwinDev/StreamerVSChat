package nl.corwindev.streamervschat.command;

import nl.corwindev.streamervschat.main;
import nl.corwindev.streamervschat.twitch.TwitchConnectionHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TwitchReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!main.plugin.getConfig().getBoolean("twitch.enabled")){
            commandSender.sendMessage(ChatColor.RED + "Twitch is not enabled.");
            return true;
        }
        TwitchConnectionHelper.reload();
        commandSender.sendMessage(ChatColor.GREEN + "Reloaded Twitch connection.");
        return true;
    }
}