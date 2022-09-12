package nl.corwindev.streamervschat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import nl.corwindev.streamervschat.commands;
public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
       if(command.getName().equalsIgnoreCase("testcommand")){
           // Check if args are empty
           if(commandSender.hasPermission("nl.corwindev.streamervschat.streamer")){
                if(strings.length == 0){
                    commandSender.sendMessage(ChatColor.RED + "Correct usage: /testcommand <msg>");
                    return false;
                }
                commands.UserList.add(commandSender.getName());
                commands.commandList.add(strings[0]);
                commandSender.sendMessage(ChatColor.GREEN + "Added command: " + strings[0] + " to queue;");
                return true;
           }
       }
       return true;
    }
}
