package com.huskydreaming.huskycore.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.List;

// TODO: Needs to be improved
public class Util {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final String regex = "\\s+";

    public static final BlockFace[] chunkSteps = new BlockFace[]{
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    public static boolean isNumeric(String string) {
        return pattern.matcher(string).matches();
    }

    public static String capitalize(String input) {
        if (input == null) return null;
        if (input.isEmpty()) return "";

        return Arrays.stream(input.split(regex))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    public static boolean areAdjacentChunks(Chunk a, Chunk b) {
        World world = a.getWorld();
        if (!world.equals(b.getWorld())) return false;

        for (BlockFace step : chunkSteps) {
            if (world.getChunkAt(a.getX() + step.getModX(), a.getZ() + step.getModZ()).equals(b)) {
                return true;
            }
        }

        return false;
    }

    public static List<Integer> getVersion() {
        String[] split = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        String majorVer = split[0];
        String minorVer = split[1];
        String minorVer2 = split.length > 2 ? split[2]:"0";
        return List.of(Integer.parseInt(majorVer), Integer.parseInt(minorVer), Integer.parseInt(minorVer2));
    }
}