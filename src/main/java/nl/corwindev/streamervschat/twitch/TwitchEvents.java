package nl.corwindev.streamervschat.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import nl.corwindev.streamervschat.commands;
import nl.corwindev.streamervschat.main;

public class TwitchEvents {

    public TwitchEvents(SimpleEventHandler eventHandler) {

        eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
    }

    private void onChannelMessage(ChannelMessageEvent event) {
        System.out.println(event.getUser().getName() + ": " + event.getMessage());
        if (event.getMessage().contains(main.plugin.getConfig().getString("commands.prefix"))) {
            commands.UserList.add(event.getUser().getName());
            commands.commandList.add(event.getMessage().replace(main.plugin.getConfig().getString("commands.prefix"), ""));
        }
    }
}