package nl.corwindev.streamervschat.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

import nl.corwindev.streamervschat.main;


import nl.corwindev.streamervschat.commands;

public class DiscordConnectionHelper {
    public static JDA api;

    public void main() throws LoginException, InterruptedException {
        api = JDABuilder.createDefault(main.plugin.getConfig().getString("discord.token"))
                .addEventListeners(new DiscordConnectionHelper.Listeners())
                .setContextEnabled(false)
                .build().awaitReady();
    }

    public static boolean isConnected() {
        if (api.getStatus() == JDA.Status.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    public class Listeners extends ListenerAdapter {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            // Get the channel the message was sent in
            TextChannel channel = event.getTextChannel();
            if (channel.getId().equals(main.plugin.getConfig().getString("discord.channel"))) {
                if (event.getAuthor().isBot()) return;
                Message message = event.getMessage();
                String content = message.getContentRaw();
                if (content.contains(main.plugin.getConfig().getString("commands.prefix"))) {
                    commands.UserList.add(event.getAuthor().getName());
                    commands.commandList.add(content.replace(main.plugin.getConfig().getString("commands.prefix"), ""));
                }
            }
        }
    }

    public static void reload(){
        main.plugin.getLogger().info("[Discord] Reloading...");
        main.plugin.reloadConfig();
        if(api.getStatus() == JDA.Status.CONNECTED){
            api.shutdown();
        }
        try {
            main.plugin.getLogger().info("[Discord] Logging in...");
            new DiscordConnectionHelper().main();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        main.plugin.getLogger().info("[Discord] Reloaded!");
    }

}
