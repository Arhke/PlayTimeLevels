package net.waterraid.PlayTimeLevels;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Listeners implements Listener {
    public static Map<UUID, Long> joinTime = new HashMap<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        joinTime.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Long time = joinTime.remove(event.getPlayer().getUniqueId());
        if (time == null || time == 0) {
            return;
        }
        PlayTimeManager.addTime(event.getPlayer().getUniqueId(), System.currentTimeMillis()-time);

    }
}
