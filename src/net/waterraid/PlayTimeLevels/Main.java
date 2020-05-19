package net.waterraid.PlayTimeLevels;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main extends JavaPlugin {
    static Main _instance;
    static File CONFIG;
    static File DATA;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        _instance = this;
        CONFIG = Paths.get(_instance.getDataFolder().toString(), "config.yml").toFile();
        DATA = Paths.get(_instance.getDataFolder().toString(), "data.yml").toFile();
        saveResource("config.yml", false);
        saveResource("data.yml", false);
        getCommand("level").setExecutor(new Command());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolderHook(this).register();
        }
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(CONFIG);
            PlayTimeManager.Rate = Math.max(config.getDouble("rate"), Double.MIN_VALUE);
            PlayTimeManager.xpBase = Math.max(config.getInt("xpbase"), 1);
            PlayTimeManager.xpIncrease = Math.max(config.getInt("xpincrease"), 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(DATA);
            PlayTimeManager.loadfromConfig(config);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player: Bukkit.getOnlinePlayers()){
                    PlayTimeManager.addTime(player.getUniqueId(), (long)(PlayTimeManager.hrconvert/60));
                }
            }
        }.runTaskTimer(Main._instance, 600, 1200);

    }

    @Override
    public void onDisable() {
        PlayTimeManager.saveToConfig(DATA);
    }
}
