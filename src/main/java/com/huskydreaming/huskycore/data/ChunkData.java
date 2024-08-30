package com.huskydreaming.huskycore.data;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Objects;

public class ChunkData {

    private final String world;
    private final int x;
    private final int z;

    public static ChunkData deserialize(String[] strings) {
        return new ChunkData(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
    }

    public static ChunkData deserialize(Chunk chunk) {
        return new ChunkData(chunk);
    }

    public ChunkData(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public ChunkData(Chunk chunk) {
        this.world = chunk.getWorld().getName();
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public Chunk toChunk() {
        World world = Bukkit.getWorld(this.world);
        if(world == null) return null;
        return world.getChunkAt(this.x, this.z);
    }

    @Override
    public String toString() {
        return world + ":" + x + ":" + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkData data)) return false;
        return x == data.x && z == data.z && Objects.equals(world, data.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }
}