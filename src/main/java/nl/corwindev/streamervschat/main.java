package nl.corwindev.streamervschat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import nl.corwindev.streamervschat.discord.DiscordConnectionHelper;
import nl.corwindev.streamervschat.twitch.TwitchConnectionHelper;
import javax.security.auth.login.LoginException;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jdk.internal.org.jline.utils.Log.debug;
import static jdk.internal.org.jline.utils.Log.error;

public final class main extends JavaPlugin {
    // Exports this class to the plugin.
    public static main plugin;
    private JdaFilter jdaFilter;

    @Override
    public void onEnable() {
        // Create config if it doesn't exist
        this.reloadConfig();
        getLogger().info("Corwin starting up!");
        this.saveDefaultConfig();
        plugin = this;
        try {
            new DiscordConnectionHelper().main();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            commands.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Start twitch bot
        if(plugin.getConfig().getString("twitch.token") != null) {
            TwitchConnectionHelper.login();
        }
        Logger.getLogger("net.dv8tion.jda.api.JDA").setLevel(Level.OFF);

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().runTask(
                this,
                () -> Bukkit.getPluginManager().disablePlugin(this)
        );
        // Plugin shutdown logic
        if(DiscordConnectionHelper.isConnected()) {
            DiscordConnectionHelper.api.shutdownNow();
        }
        if(TwitchConnectionHelper.isConnected()) {
            TwitchConnectionHelper.getBot().disconnect();
        }
        getLogger().info("Corwin shutting down!");

    }
}
