package nl.corwindev.streamervschat;

import nl.corwindev.streamervschat.command.DiscordReload;
import nl.corwindev.streamervschat.command.TwitchReload;
import nl.corwindev.streamervschat.command.YouTubeReload;
import nl.corwindev.streamervschat.objects.UpdateChecker;
import nl.corwindev.streamervschat.youtube.YouTubeConnectionHelper;
import org.bstats.charts.DrilldownPie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import nl.corwindev.streamervschat.discord.DiscordConnectionHelper;
import nl.corwindev.streamervschat.twitch.TwitchConnectionHelper;
import javax.security.auth.login.LoginException;
import nl.corwindev.streamervschat.objects.JdaFilter;
import nl.corwindev.streamervschat.command.TestCommand;
import org.bstats.bukkit.Metrics;

import java.util.HashMap;
import java.util.Map;

public final class main extends JavaPlugin {
    // Exports this class to the plugin.
    public static main plugin;
    private JdaFilter jdaFilter;

    @Override
    public void onEnable() {
        this.reloadConfig();
        this.saveDefaultConfig();
        plugin = this;
        int pluginId = 16419;
        new UpdateChecker(this, pluginId).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().warning("There is a new update available. Download it here: https://www.spigotmc.org/resources/streamer-vs-chat-1-13-1-19.105119/");
            }
        });
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
        if (plugin.getConfig().getBoolean("youtube.enabled")) {
            YouTubeConnectionHelper.main("run");
        }
        this.getCommand("testcommand").setExecutor(new TestCommand());
        this.getCommand("youtube-reload").setExecutor(new YouTubeReload());
        this.getCommand("twitch-reload").setExecutor(new TwitchReload());
        this.getCommand("discord-reload").setExecutor(new DiscordReload());
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new DrilldownPie("live_platform", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Boolean YouTubeEnabled = this.getConfig().getBoolean("youtube.enabled");
            Boolean TwitchEnabled = this.getConfig().getBoolean("twitch.enabled");
            Boolean DiscordEnabled = this.getConfig().getBoolean("discord.enabled");
            Map<String, Integer> entry = new HashMap<>();
            entry.put("YouTube", YouTubeEnabled ? 1 : 0);
            entry.put("Twitch", TwitchEnabled ? 1 : 0);
            entry.put("Discord", DiscordEnabled ? 1 : 0);
            if(YouTubeEnabled) {
                map.put("YouTube", entry);
            }
            if(TwitchEnabled) {
                map.put("Twitch", entry);
            }
            if(DiscordEnabled) {
                map.put("Discord", entry);
            }
            return map;
        }));
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
        YouTubeConnectionHelper.stop();

        getLogger().info("Corwin shutting down!");

    }
}
