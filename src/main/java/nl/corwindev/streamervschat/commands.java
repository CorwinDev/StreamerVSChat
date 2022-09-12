package nl.corwindev.streamervschat;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static nl.corwindev.streamervschat.main.plugin;

public class commands {
    public static List<String> commandList = new ArrayList<String>();
    public static List<String> UserList = new ArrayList<String>();
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
                // Alert the user
                for(Player player: getPlayers()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a§l" + UserList.get(randomCommand) + "§r§a has executed the command: §l" + command));
                }
            }
        }, plugin.getConfig().getInt("commands.delay") * 20, plugin.getConfig().getInt("commands.delay") * 20);
    }

    public static void runCmd(String command) {
        commandList.clear();
        if(Objects.requireNonNull(plugin.getConfig().getList("blacklist")).contains(command)){
            return;
        }
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
        }else if (Objects.equals(command, "heal")){
            commands.heal();
        } else if (Objects.equals(command,"feed")){
            commands.feed();
        }else if(Objects.equals(command, "jumpboost")){
            commands.jumpboost();
        }else if(Objects.equals(command, "levitate") || Objects.equals(command, "fly")) {
            commands.fly();
        }else if(Objects.equals(command, "randomeffect") || Objects.equals(command, "randompoison")){
            commands.randomeffect();
        }else if(Objects.equals(command, "fireball")) {
            commands.fireball();
        }else if(Objects.equals(command, "drop")){
            commands.drop();
        } else {
            commands.custom(command);
        }
    }

    public static String name = "nl.corwindev.streamervschat";
    public static Integer duration = plugin.getConfig().getInt("poison-duration");
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
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, duration * 20, 1));
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
            TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
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
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HUNGER, duration * 20, 1));
        }
    }

    public static void badluck(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.UNLUCK, duration * 20, 1));
        }
    }

    public static void nausea(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.CONFUSION, duration * 20, 1));
        }
    }

    public static void slowness(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW, duration * 20, 1));
        }
    }

    public static void miningfatigue(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW_DIGGING, duration * 20, 1));
        }
    }

    public static void feed(){
        for (Player player : getPlayers()) {
            player.setFoodLevel(20);
        }
    }

    public static void heal(){
        //Gives the player regeneration and health boost 2
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.REGENERATION, duration * 20, 1));
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HEALTH_BOOST, duration * 20, 1));
        }
    }

    public static void jumpboost(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.JUMP, duration * 20, 1));
        }
    }

    public static void fly(){
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.LEVITATION, duration * 20, 1));
        }
    }

    public static void custom(String command){
        String command1 = plugin.getConfig().getString("customcommands." + command + ".command");
        if(command1 != null){
            for (Player player : getPlayers()) {
                String command2 = command1.replace("%player%", player.getName());
                plugin.getLogger().info(command2);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2);
            }
        }
    }

    public static void randomeffect(){
        int rnd = ThreadLocalRandom.current().nextInt(PotionEffectType.values().length);

        for (Player player : getPlayers()) {
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.values()[rnd], 200, 1));
        }
    }

    public static void fireball(){
        for (Player player : getPlayers()) {
            // Send fireball to the player
            Location location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 20, player.getLocation().getZ() );
            Vector vector = player.getLocation().toVector().subtract(location.toVector()).normalize();
            Fireball fire = (Fireball) location.getWorld().spawn(location, Fireball.class);
            fire.setDirection(vector);
        }
    }

    public static void drop(){
        for (Player player : getPlayers()) {
            ItemStack hand = player.getInventory().getItemInMainHand();
            player.getInventory().removeItem(hand);
            player.getWorld().dropItemNaturally(player.getLocation(), hand);
        }
    }
}