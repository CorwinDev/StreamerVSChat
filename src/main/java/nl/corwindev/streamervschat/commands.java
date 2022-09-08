package nl.corwindev.streamervschat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
                if (commandList.size() == 0) {
                    return;
                }
                int randomCommand = rand.nextInt(commandList.size());
                String command = commandList.get(randomCommand);
                if (!command.isEmpty()) {
                    commandList.clear();
                }
                // Remove the command from the list
                runCmd(command);
            }
        }, plugin.getConfig().getInt("commands.delay") * 20, plugin.getConfig().getInt("commands.delay") * 20);
    }

    public static void runCmd(String command) {
        commandList.clear();
        if (Objects.equals(command, "lava")) {
            commands.lava();
        } else if (Objects.equals(command, "anvil")) {
            commands.anvil();
        } else if (Objects.equals(command, "creepscare") || Objects.equals(command, "behindyou")) {
            commands.creepsound();
        } else if (Objects.equals(command, "dropall")) {
            commands.dropall();
        } else if (Objects.equals(command, "blindness")) {
            commands.blindness();
        } else if (Objects.equals(command, "lightning")) {
            commands.lightning();
        } else if (Objects.equals(command, "fire")) {
            commands.fire();
        } else if (Objects.equals(command, "explosion")) {
            commands.explosion();
        } else if (Objects.equals(command, "rain")) {
            commands.rain();
        } else if (Objects.equals(command, "hunger")) {
            commands.hunger();
        } else if (Objects.equals(command, "witherscare") || Objects.equals(command, "wither")) {
            commands.wither();
        } else if (Objects.equals(command, "creeper")) {
            commands.creeper();
        } else if (Objects.equals(command, "zombie")) {
            commands.zombie();
        }else if(Objects.equals(command, "illness") || Objects.equals(command, "nausea")){
            commands.nausea();
        }else if(Objects.equals(command, "slowness")){
            commands.slowness();
        }else if(Objects.equals(command, "badluck")){
            commands.badluck();
        }else if(Objects.equals(command, "mining") || Objects.equals(command, "miningfatigue")){
            commands.miningfatigue();
        }
    }

    public static String name = "nl.corwindev.streamervschat";

    public static boolean has(Player player, String permission) {
        return player == null || player.hasPermission(name + "." + permission) || player.hasPermission(name + ".*");
    }

    public static Collection<Player> getPlayers() {
        Collection<Player> players = new ArrayList<>();
        if(Objects.equals(plugin.getConfig().getString("affected-players"), "all")) {
            return (Collection<Player>) Bukkit.getOnlinePlayers();
        } else if(Objects.equals(plugin.getConfig().getString("affected-players"), "permission")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (has(player, "streamer")) {
                    players.add(player);
                }
            }
            return players;
        }else{
            return (Collection<Player>) Bukkit.getOnlinePlayers();
        }
    }

    static World w = Bukkit.getWorld("world");

    public static void lava() {
        for (Player player : getPlayers()) {
            Block playerBlock = player.getLocation().getBlock();
            Block blockUnder = playerBlock.getRelative(BlockFace.DOWN);
            // Set block to lava
            blockUnder.setType(org.bukkit.Material.LAVA);
        }
    }

    public static void anvil() {
        for (Player player : getPlayers()) {
            FallingBlock anvil = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 16, 0), Bukkit.createBlockData(Material.ANVIL));
            anvil.setGravity(true);
            anvil.setHurtEntities(true);
            anvil.setFallDistance(20);
            anvil.setDropItem(false);
            player.getWorld().playSound(player.getLocation(), "minecraft:block.anvil.land", 100, 1);
        }
    }

    public static void creepsound() {
        for (Player player : getPlayers()) {
            player.getWorld().playSound(player.getLocation(), "minecraft:entity.creeper.primed", 100, 1);
            player.getWorld().playSound(player.getLocation(), "minecraft:entity.creeper.primed", 100, 1);
        }
    }

    public static void dropall() {
        for (Player player : getPlayers()) {
            Location loc = player.getLocation().clone();
            Inventory inv = player.getInventory();
            for (ItemStack item : inv.getContents()) {
                if (item != null) {
                    loc.getWorld().dropItemNaturally(loc, item.clone());
                }
            }
            inv.clear();
        }
    }

    public static void blindness() {
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 20 * 10, 1));
        }
    }

    public static void lightning() {
        for (Player player : getPlayers()) {
            player.getWorld().strikeLightning(player.getLocation());
        }
    }

    public static void creeper() {
        for (Player player : getPlayers()) {
            player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Creeper.class);
        }
    }

    public static void zombie() {
        for (Player player : getPlayers()) {
            player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Zombie.class);
        }
    }

    public static void fire() {
        for (Player player : getPlayers()) {
            player.setFireTicks(20 * 10);
        }
    }

    public static void explosion() {
        for (Player player : getPlayers()) {
            TNTPrimed tnt = (TNTPrimed) w.spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
            tnt.setFuseTicks(40);
        }
    }

    public static void rain() {
        for (Player player : getPlayers()) {
            player.getWorld().setStorm(true);
        }
    }

    public static void wither() {
        for (Player player : getPlayers()) {
            player.getWorld().playSound(player.getLocation(), "minecraft:entity.wither.spawn", 100, 1);
        }
    }

    public static void hunger() {
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HUNGER, 20 * 30, 1));
        }
    }

    public static void badluck(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.UNLUCK, 20 * 30, 1));
        }
    }

    public static void nausea(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.CONFUSION, 20 * 30, 1));
        }
    }

    public static void slowness(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW, 20 * 30, 1));
        }
    }

    public static void miningfatigue(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW_DIGGING, 20 * 30, 1));
        }
    }
}