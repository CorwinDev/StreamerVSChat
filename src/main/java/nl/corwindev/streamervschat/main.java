package nl.corwindev.streamervschat;

import org.bukkit.plugin.java.JavaPlugin;
import nl.corwindev.streamervschat.discord.DiscordConnectionHelper;
import nl.corwindev.streamervschat.twitch.TwitchConnectionHelper;
import javax.security.auth.login.LoginException;

import static java.util.logging.Level.OFF;

public final class main extends JavaPlugin {
    // Exports this class to the plugin.
    public static main plugin;

    @Override
    public void onEnable() {
        // Create config if it doesn't exist
        this.reloadConfig();
        getLogger().info("Corwin starting up!");
        this.getLogger("net.dv8tion.jda.api.JDA").setLevel(OFF);
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
