package net.waterraid.PlayTimeLevels;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayTimeManager {
    public static Map<UUID, Double> TimeLog = new HashMap<UUID, Double>(); //in hours
    public static double Rate;
    public static int xpBase;
    public static int xpIncrease;

    public static void loadfromConfig(YamlConfiguration config) {
        for (String key : config.getKeys(false)) {
            UUID id = UUID.fromString(key);
            Double time = config.getDouble(id + ".time");
            TimeLog.put(id, time);
        }
    }

    public static void saveToConfig(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, Double> entry : TimeLog.entrySet()) {
            config.set(entry.getKey().toString() + ".time", entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addTime(UUID uuid, long milli) {
        TimeLog.put(uuid, TimeLog.get(uuid) + ((double) milli / 3600000.0d));
    }

    public static double getTime(UUID uuid) {
        return TimeLog.getOrDefault(uuid, 0d);
    }

    public static int[] getStat(UUID uuid) {
        int[] ret = new int[4];
        double time = getTime(uuid);
        int xp = getAbsoluteXP(time);

        int level = 0;
        while (leveltoxp(level) < xp) {
            level++;
        }
        ret[0] = (int) time;
        ret[1] = xp;
        ret[2] = level;
        ret[3] = (int)(xp-leveltoxp(level));
        return ret;
    }

    private static int getAbsoluteXP(double time) {
        return (int) (time * Rate);
    }

    public static void addXp(UUID id, int xp) {
        TimeLog.put(id, ((double) xp) / Rate + TimeLog.getOrDefault(id, 0d));
    }

    public static void addLevel(UUID id, int levels) {
        int level = getLevel(id) + levels;
        TimeLog.put(id, leveltoxp(level)/2.2);
    }

    public static int getLevel(UUID id) {
        return getLevel(TimeLog.getOrDefault(id, 0d));
    }

    private static int getLevel(double hrs) {
        int xp = getAbsoluteXP(hrs);
        int level = 0;
        while (leveltoxp(level) < xp)
            level++;
        return level;
    }

    public static int getLevelUpXp(int level) {
        return Math.max(xpBase + xpIncrease * level, 0);
    }
    public static double leveltoxp(int level){
        return ((double) xpIncrease) / 2d * Math.pow(level, 2) + ((double)(2 * xpBase - xpIncrease)) / 2 * level ;

    }

}
