package com.huskydreaming.huskycore.utilities;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public class ChunkUtil {

    public static final BlockFace[] chunkSteps = new BlockFace[] {
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    public static boolean areAdjacentChunks(Chunk a, Chunk b) {
        World world = a.getWorld();
        if (!world.equals(b.getWorld())) return false;

        for (BlockFace step : ChunkUtil.chunkSteps) {
            if (world.getChunkAt(a.getX() + step.getModX(), a.getZ() + step.getModZ()).equals(b)) {
                return true;
            }
        }

        return false;
    }
}