package net.waterraid.PlayTimeLevels;

import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONObject;

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
    public static double hrconvert = 3600000d;

    public static void loadfromConfig(YamlConfiguration config) {
        for (String key : config.getKeys(false)) {
            TimeLog.put(UUID.fromString(key), config.getDouble(key));
        }
    }

    public static void saveToConfig(File file) {
        Main._instance.saveResource(file.getName(), false);
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, Double> entry : TimeLog.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addTime(UUID uuid, long milli) {
        TimeLog.put(uuid, getTime(uuid) + milli / hrconvert);
    }

    public static double getTime(UUID uuid) {
        return TimeLog.getOrDefault(uuid, 0d);
    }

    public static JSONObject getStat(UUID uuid) {
        JSONObject json = new JSONObject();
        double time = getTime(uuid);
        int xp = getAbsoluteXP(time);
        int level = getLevel(time);
        json.put("time", (int) time);
        json.put("xp", xp);
        json.put("level", level);
        json.put("relativexp", (int)(xp-leveltoxp(level-1)));
        return json;
    }

    private static int getAbsoluteXP(double time) {
        return (int)(time * Rate);
    }

    public static void addXp(UUID id, int xp) {
        TimeLog.put(id,  xp / Rate + getTime(id));
    }

    public static void addLevel(UUID id, int levels) {
        int level = getLevel(id) + levels;
        TimeLog.put(id, leveltoxp(level)/Rate);
    }

    public static int getLevel(UUID id) {
        return getLevel(getTime(id));
    }

    private static int getLevel(double hrs) {
        int xp = getAbsoluteXP(hrs);
        int level = 1;
        while (leveltoxp(level) < xp)
            level++;
        return level;
    }

    public static int getLevelUpXp(int level) {
        return xpBase + xpIncrease * (level-1);
    }
    public static int leveltoxp(int level){
        return level*xpBase + (level-1)*level*xpIncrease/2;

    }

}
