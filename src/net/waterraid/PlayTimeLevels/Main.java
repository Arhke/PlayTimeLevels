package net.waterraid.PlayTimeLevels;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    static Main _instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        _instance = this;
        saveResource("config.yml", false);
        saveResource("data.yml", false);
        getCommand("level").setExecutor(new Command());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolderHook(this).register();
        }
        try {
            YamlConfiguration config = new YamlConfiguration();
            File file = new File(this.getDataFolder() + File.separator + "config.yml");
            config.load(file);
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
            File file = new File(this.getDataFolder() + File.separator + "data.yml");
            config.load(file);
            PlayTimeManager.loadfromConfig(config);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        File file = new File(this.getDataFolder() + File.separator + "data.yml");
        PlayTimeManager.saveToConfig(file);
    }
}
