package nl.corwindev.streamervschat;

import nl.corwindev.streamervschat.discord.DiscordConnectionHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static nl.corwindev.streamervschat.main.plugin;

public class commands {
    public static List<String> commandList = new ArrayList<String>();
    public static void start() {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                // Pick a random command from the list
                Random rand = new Random();
                if(commandList.size() == 0) {
                    return;
                }
                int randomCommand = rand.nextInt(commandList.size());
                String command = commandList.get(randomCommand);
                if(!command.isEmpty()) {
                    commandList.clear();
                }
                // Remove the command from the list
                commandList.clear();
                if (Objects.equals(command, "lava")) {
                    commands.lava();
                } else if (Objects.equals(command, "anvil")) {
                    commands.anvil();
                } else if (Objects.equals(command, "creepscare") || Objects.equals(command, "behindyou")){
                    commands.creepsound();
                } else if(Objects.equals(command, "dropall")){
                    commands.dropall();
                }else if(Objects.equals(command, "blindness")) {
                    commands.blindness();
                }else if(Objects.equals(command, "lightning")) {
                    commands.lightning();
                }
            }
        }, plugin.getConfig().getInt("commands.delay") * 20, plugin.getConfig().getInt("commands.delay") * 20);
    }

    static World w = Bukkit.getWorld("world");

    public static void lava() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Block playerBlock = player.getLocation().getBlock();
            Block blockUnder = playerBlock.getRelative(BlockFace.DOWN);
            // Set block to lava
            blockUnder.setType(org.bukkit.Material.LAVA);
        }
    }

    public static void anvil() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            FallingBlock anvil = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 16, 0), Bukkit.createBlockData(Material.ANVIL));
            anvil.setGravity(true);
            anvil.setHurtEntities(true);
            anvil.setFallDistance(20);
            anvil.setDropItem(false);
            player.getWorld().playSound(player.getLocation(), "minecraft:block.anvil.land", 100, 1);
        }
    }
    public static void creepsound(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getWorld().playSound(player.getLocation(), "minecraft:entity.creeper.primed", 100, 1);
            player.getWorld().playSound(player.getLocation(), "minecraft:entity.creeper.primed", 100, 1);
        }
    }
    public static void dropall(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getWorld().dropItem(player.getLocation(), player.getInventory().getItemInMainHand());
            player.getWorld().dropItem(player.getLocation(), player.getInventory().getItemInOffHand());
            player.getInventory().clear();
        }
    }
    public static void blindness(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 20 * 10, 1));
        }
    }
    public static void lightning(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getWorld().strikeLightning(player.getLocation());
        }
    }
}