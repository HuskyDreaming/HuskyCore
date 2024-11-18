package com.huskydreaming.huskycore.utilities;

import org.bukkit.Bukkit;

import java.util.List;

public class Util {

    public static List<Integer> getVersion() {
        String[] split = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        String majorVer = split[0];
        String minorVer = split[1];
        String minorVer2 = split.length > 2 ? split[2] : "0";
        return List.of(Integer.parseInt(majorVer), Integer.parseInt(minorVer), Integer.parseInt(minorVer2));
    }
}