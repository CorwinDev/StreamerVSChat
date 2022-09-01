package nl.corwindev.forever;

import org.bukkit.loot.LootTables;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class commands {
    public static void lava(){
        // Get plu
        BlockPos bpos = new BlockPos(player.getX(), player.getY() - 1, player.getZ());
        setBlock(bpos, Blocks.LAVA.defaultBlockState());
    }

}