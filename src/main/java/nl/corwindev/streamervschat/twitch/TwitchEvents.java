package nl.corwindev.streamervschat.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import net.dv8tion.jda.api.entities.Message;
import nl.corwindev.streamervschat.commands;
import nl.corwindev.streamervschat.main;

public class TwitchEvents {

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public TwitchEvents(SimpleEventHandler eventHandler) {

        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);/*
        eventHandler.onEvent(ChannelJoinEvent.class, this::onConnect);
        eventHandler.onEvent(CheerEvent.class, this::onCheer);
        */
    }

    /**
     * Event: ChannelMessageEvent
     *
     * @param event ChannelMessageEvent
     */
    private void onChannelMessage(ChannelMessageEvent event) {
        // Send message to chat
        //event.getChannel().sendMessage("Hello World!");
        // Send message to console
        System.out.println(event.getUser().getName() + ": " + event.getMessage());
        if (event.getMessage().contains(main.plugin.getConfig().getString("commands.prefix"))) {
            commands.commandList.add(event.getMessage().replace(main.plugin.getConfig().getString("commands.prefix"), ""));
        }
    }
}