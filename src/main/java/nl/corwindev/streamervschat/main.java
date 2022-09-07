package nl.corwindev.streamervschat;

import nl.corwindev.streamervschat.youtube.YouTubeConnectionHelper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import nl.corwindev.streamervschat.discord.DiscordConnectionHelper;
import nl.corwindev.streamervschat.twitch.TwitchConnectionHelper;
import javax.security.auth.login.LoginException;
import nl.corwindev.streamervschat.objects.JdaFilter;

public final class main extends JavaPlugin {
    // Exports this class to the plugin.
    public static main plugin;
    private JdaFilter jdaFilter;

    @Override
    public void onEnable() {
        this.reloadConfig();
        this.saveDefaultConfig();
        plugin = this;
        boolean serverIsLog4jCapable = false;
        boolean serverIsLog4j21Capable = false;
        try {
            serverIsLog4jCapable = Class.forName("org.apache.logging.log4j.core.Logger") != null;
        } catch (ClassNotFoundException e) {
            getLogger().info("Log4j classes are NOT available, console channel will not be attached");
        }
        try {
            serverIsLog4j21Capable = Class.forName("org.apache.logging.log4j.core.Filter") != null;
        } catch (ClassNotFoundException e) {
            getLogger().info("Log4j 2.1 classes are NOT available, JDA messages will NOT be formatted properly");
        }

        // add log4j filter for JDA messages
        if (serverIsLog4j21Capable && jdaFilter == null) {
            try {
                Class<?> jdaFilterClass = Class.forName("nl.corwindev.streamervschat.objects.JdaFilter");
                jdaFilter = (JdaFilter) jdaFilterClass.newInstance();
                ((org.apache.logging.log4j.core.Logger) org.apache.logging.log4j.LogManager.getRootLogger()).addFilter((org.apache.logging.log4j.core.Filter) jdaFilter);
                getLogger().info("ConsoleFilter Attached");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (plugin.getConfig().getBoolean("twitch.enabled")) {
            TwitchConnectionHelper.login();
        }
        if (plugin.getConfig().getBoolean("discord.enabled")) {
            try {
                new DiscordConnectionHelper().main();
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!plugin.getConfig().getBoolean("twitch.enabled") && !plugin.getConfig().getBoolean("discord.enabled") && !plugin.getConfig().getBoolean("youtube.enabled")) {
            getLogger().info("No services enabled, disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            try {
                commands.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (plugin.getConfig().getBoolean("youtube.enabled")) {
            YouTubeConnectionHelper.main("test");
        }
    }

    @Override
    public void onDisable() {
        if(DiscordConnectionHelper.api != null) {
            if (DiscordConnectionHelper.isConnected()) {
                DiscordConnectionHelper.api.shutdown();
            }
        }
        if (TwitchConnectionHelper.isConnected()) {
            TwitchConnectionHelper.getBot().disconnect();
        }
        getLogger().info("Corwin shutting down!");

    }
}
