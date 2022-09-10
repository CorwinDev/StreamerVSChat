package nl.corwindev.streamervschat.twitch;


import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.TwitchChatBuilder;
import com.github.twitch4j.chat.enums.TMIConnectionState;
import nl.corwindev.streamervschat.main;

import java.util.function.Supplier;

import static nl.corwindev.streamervschat.main.plugin;

public class TwitchConnectionHelper {
    private static Thread botThread = null;
    private static TwitchChat twitchClient = null;

    public static TwitchChat getBot() {
        return twitchClient;
    }
    public static boolean login() {
        try {
            OAuth2Credential credential = new OAuth2Credential("twitch", plugin.getConfig().getString("twitch.token"));
            twitchClient = TwitchChatBuilder.builder()
                    .withChatAccount(credential)
                    .withDefaultEventHandler(SimpleEventHandler.class)
                    .build();


            botThread = new Thread(() -> {

                TwitchEvents bot = new TwitchEvents(twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class));
                twitchClient.joinChannel(plugin.getConfig().getString("twitch.channel"));
                twitchClient.connect();

            });

            botThread.start();
            return true;

        } catch (Exception e) {
            // Log logger to console

            plugin.getLogger().warning((Supplier<String>) e);
            return false;
        }
    }
    public static boolean isConnected() {

        if (twitchClient != null) {
            return twitchClient.getConnectionState().equals(TMIConnectionState.CONNECTED);
        } else {
            return false;
        }

    }

    public static void disconnectBot() {

        twitchClient.disconnect();
        botThread.interrupt();

        plugin.getLogger().info("Shutted down twitch bot");
    }

    public static void reload(){
        main.plugin.getLogger().info("[Twitch] Reloading...");
        main.plugin.reloadConfig();
        if(isConnected()){
            disconnectBot();
        }
        main.plugin.getLogger().info("[Twitch] Reloaded!");
    }
}
