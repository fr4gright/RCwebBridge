package me.rvt.rcwebbridge;

import me.rvt.rcwebbridge.extractor.Stats;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class RCwebBridge extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        //commandReader(this, 10);
    }

    @EventHandler
    private void PlayerQuitEvent(PlayerQuitEvent e)
    {
        new Stats(e.getPlayer());
    }

    @EventHandler
    private void PlayerConnectEvent(PlayerJoinEvent e)
    {
        new Stats(e.getPlayer());
    }
}