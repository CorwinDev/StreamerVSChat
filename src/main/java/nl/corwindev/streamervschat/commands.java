package nl.corwindev.streamervschat;

import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    public static List<String> cooldowns = new ArrayList<String>();

    public static void start() {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                startCommand();
            }
        }, plugin.getConfig().getInt("commands.delay") * 20L, plugin.getConfig().getInt("commands.delay") * 20L);
    }
    public static void startCommand(){
        Random rand = new Random();
        if (commandList.size() == 0) {
            return;
        }
        int randomCommand = rand.nextInt(commandList.size());
        String command = commandList.get(randomCommand);
        runCmd(command, UserList.get(randomCommand));
    }
    public static void runCmd(String command, String random) {
        if (Objects.requireNonNull(plugin.getConfig().getList("blacklist")).contains(command)) {
            return;
        }
        int index = commandList.indexOf(command);
        if(plugin.getConfig().getString("cooldowns." + command) != null){
            if(cooldowns.contains(command)){
                commandList.remove(index);
                UserList.remove(index);
                startCommand();
                return;
            }else{
                cooldowns.add(command);
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        cooldowns.remove(command);
                    }
                }, plugin.getConfig().getInt("cooldowns." + command) * 20L);
            }
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
            commands.creeper(UserList.get(index));
        } else if (Objects.equals(command, "zombie")) {
            commands.zombie(UserList.get(index));
        } else if (Objects.equals(command, "illness") || Objects.equals(command, "nausea")) {
            commands.nausea();
        } else if (Objects.equals(command, "slowness")) {
            commands.slowness();
        } else if (Objects.equals(command, "badluck")) {
            commands.badluck();
        } else if (Objects.equals(command, "mining") || Objects.equals(command, "miningfatigue")) {
            commands.miningfatigue();
        } else if (Objects.equals(command, "heal")) {
            commands.heal();
        } else if (Objects.equals(command, "feed")) {
            commands.feed();
        } else if (Objects.equals(command, "jumpboost")) {
            commands.jumpboost();
        } else if (Objects.equals(command, "levitate") || Objects.equals(command, "fly")) {
            commands.fly();
        } else if (Objects.equals(command, "randomeffect") || Objects.equals(command, "randompoison")) {
            commands.randomeffect();
        } else if (Objects.equals(command, "fireball")) {
            commands.fireball();
        } else if (Objects.equals(command, "drop")) {
            commands.drop();
        } else if (Objects.equals(command, "silverfish")) {
            commands.silverfish(UserList.get(index));
        } else if (Objects.equals(command, "vex")) {
            commands.vex(UserList.get(index));
        } else if (Objects.equals(command, "chicken")) {
            commands.chicken(UserList.get(index));
        } else if (Objects.equals(command, "bee")) {
            commands.bee(UserList.get(index));
        }else if(Objects.equals(command, "day")){
            commands.day();
        }else if(Objects.equals(command, "night")) {
            commands.night();
        }else if(Objects.equals(command, "peaceful")) {
            commands.peaceful();
        }else if(Objects.equals(command, "hard")) {
            commands.hard();
        }else if(Objects.equals(command, "easy")) {
            commands.easy();
        }else if(command.startsWith("rename")){
            commands.rename(command);
        } else {
            if(!commands.custom(command, UserList.get(index))){
                commandList.remove(index);
                UserList.remove(index);
                startCommand();
                return;
            }
        }
        for (Player player : getPlayers()) {
            String hotbar = plugin.getConfig().getString("hotbar");
            if(hotbar == null){
                hotbar = "§a§l%user%§r§a has executed the command: §l%command%";
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(hotbar.replace("%command%", command).replace("%user%", random)));
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a§l" + random + "§r§a has executed the command: §l" + command));
        }
        commandList.clear();
        UserList.clear();
    }

    public static String name = "nl.corwindev.streamervschat";
    public static Integer duration = plugin.getConfig().getInt("poison-duration");

    public static boolean has(Player player, String permission) {
        return player == null || player.hasPermission(name + "." + permission) || player.hasPermission(name + ".*");
    }

    public static Collection<Player> getPlayers() {
        Collection<Player> players = new ArrayList<>();
        if (Objects.equals(plugin.getConfig().getString("affected-players"), "all")) {
            return (Collection<Player>) Bukkit.getOnlinePlayers();
        } else if (Objects.equals(plugin.getConfig().getString("affected-players"), "permission")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (has(player, "streamer")) {
                    players.add(player);
                }
            }
            return players;
        } else {
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

    public static void creeper(String user) {
        for (Player player : getPlayers()) {
            player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Creeper.class).setCustomName("§c§l" + user);
        }
    }

    public static void zombie(String user) {
        for (Player player : getPlayers()) {
            player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Zombie.class).setCustomName("§c§l" + user);
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

    public static void badluck() {
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.UNLUCK, duration * 20, 1));
        }
    }

    public static void nausea() {
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.CONFUSION, duration * 20, 1));
        }
    }

    public static void slowness() {
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW, duration * 20, 1));
        }
    }

    public static void miningfatigue() {
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW_DIGGING, duration * 20, 1));
        }
    }

    public static void feed() {
        for (Player player : getPlayers()) {
            player.setFoodLevel(20);
        }
    }

    public static void heal() {
        //Gives the player regeneration and health boost 2
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.REGENERATION, duration * 20, 1));
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HEALTH_BOOST, duration * 20, 1));
        }
    }

    public static void jumpboost() {
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.JUMP, duration * 20, 1));
        }
    }

    public static void fly() {
        for (Player player : getPlayers()) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.LEVITATION, duration * 20, 1));
        }
    }

    public static boolean custom(String command, String user) {
        String command1 = plugin.getConfig().getString("customcommands." + command + ".command");
        if (command1 != null) {
            Integer cooldown = plugin.getConfig().getInt("customcommands." + command + ".cooldown");

            if (cooldown != null) {
                if (cooldowns.contains(command)) {
                    return false;
                }
                cooldowns.add(command);
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        cooldowns.remove(command);
                    }
                }, cooldown * 20);
            }
            for (Player player : getPlayers()) {
                String command2 = command1.replace("%player%", player.getName());
                String command3 = command2.replace("%user%", user);
                plugin.getLogger().info(command3);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2);
            }
            return true;
        }
        return false;
    }

    public static void randomeffect() {
        int rnd = ThreadLocalRandom.current().nextInt(PotionEffectType.values().length);

        for (Player player : getPlayers()) {
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.values()[rnd], 200, 1));
        }
    }

    public static void fireball() {
        for (Player player : getPlayers()) {
            // Send fireball to the player
            Location location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 20, player.getLocation().getZ());
            Vector vector = player.getLocation().toVector().subtract(location.toVector()).normalize();
            Fireball fire = (Fireball) location.getWorld().spawn(location, Fireball.class);
            fire.setDirection(vector);
        }
    }

    public static void drop() {
        for (Player player : getPlayers()) {
            ItemStack hand = player.getInventory().getItemInMainHand();
            player.getInventory().removeItem(hand);
            player.getWorld().dropItemNaturally(player.getLocation(), hand);
        }
    }

    public static void silverfish(String user) {
        for (Player player : getPlayers()) {
            player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Silverfish.class).setCustomName("§c§l" + user);;
        }
    }

    public static void vex(String user) {
        for (Player player : getPlayers()) {
            player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Vex.class).setCustomName("§c§l" + user);;
        }
    }

    public static void bee(String user) {
        for (Player player : getPlayers()) {
            // Make bee angry
            Bee bee = (Bee) player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Bee.class);
            bee.setAnger(1000000);
            bee.setCustomName("§c§l" + user);
            bee.attack(player);
        }
    }

    public static void chicken(String user) {
        for (Player player : getPlayers()) {
            player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Chicken.class).setCustomName("§c§l" + user);;
        }
    }

    public static void day() {
        for (Player player : getPlayers()) {
            player.getWorld().setTime(0);
        }
    }

    public static void night() {
        for (Player player : getPlayers()) {
            player.getWorld().setTime(14000);
        }
    }

    public static void peaceful() {
        for (Player player : getPlayers()) {
            player.getWorld().setDifficulty(Difficulty.PEACEFUL);
        }
    }

    public static void hard() {
        for (Player player : getPlayers()) {
            player.getWorld().setDifficulty(Difficulty.HARD);
        }
    }

    public static void easy() {
        for (Player player : getPlayers()) {
            player.getWorld().setDifficulty(Difficulty.EASY);
        }
    }

    public static void rename(String command) {
        String name = command.substring(7);
        for (Player player : getPlayers()) {
            ItemStack hand = player.getInventory().getItemInMainHand();
            ItemMeta meta = hand.getItemMeta();
            assert meta != null;
            int rnd = ThreadLocalRandom.current().nextInt(16);
            meta.setDisplayName(ChatColor.values()[rnd] + name);
            hand.setItemMeta(meta);
        }
    }
}