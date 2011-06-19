package org.bukkitcontrib;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkitcontrib.event.bukkitcontrib.BukkitContribSPEnable;
import org.bukkitcontrib.player.ContribCraftPlayer;
import org.bukkitcontrib.player.ContribPlayer;
import org.bukkitcontrib.player.SimpleAppearanceManager;

public class ContribPlayerListener extends PlayerListener{
    public PlayerManager manager = new PlayerManager();
    @Override
    public void onPlayerJoin(final PlayerJoinEvent event) {
        ContribCraftPlayer.updateNetServerHandler(event.getPlayer());
        ContribCraftPlayer.updateBukkitEntity(event.getPlayer());
        updatePlayerEvent(event);
        BukkitContrib.sendBukkitContribVersionChat(event.getPlayer());
        manager.onPlayerJoin(event.getPlayer());
    }

    @Override
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) {
            Runnable update = new Runnable() {
                public void run() {
                    ContribCraftPlayer.updateBukkitEntity(event.getPlayer());
                }
            };
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BukkitContrib.getInstance(), update);
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock() != null) {
            Material type = event.getClickedBlock().getType();
            ContribCraftPlayer player = (ContribCraftPlayer) ContribCraftPlayer.getContribPlayer(event.getPlayer());
            if (type == Material.CHEST || type == Material.DISPENSER || type == Material.WORKBENCH || type == Material.FURNACE) {
                player.getNetServerHandler().activeLocation = event.getClickedBlock().getLocation();
            }
        }
    }
    
    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        ContribCraftPlayer player = (ContribCraftPlayer)ContribCraftPlayer.getContribPlayer(event.getPlayer());
        if (player.isEnabledBukkitContribSinglePlayerMod()) {
            return;
        }
        if (event.getMessage().split("\\.").length == 3) {
            player.setVersion(event.getMessage().substring(1));
            if (player.isEnabledBukkitContribSinglePlayerMod()) {
                event.setCancelled(true);
                ((SimpleAppearanceManager)BukkitContrib.getAppearanceManager()).onPlayerJoin((ContribPlayer)event.getPlayer());
                manager.onBukkitContribSPEnable(player);
                Bukkit.getServer().getPluginManager().callEvent(new BukkitContribSPEnable(player));
            }
        }
    }
    
    private void updatePlayerEvent(PlayerEvent event) {
        try {
            Field player = PlayerEvent.class.getDeclaredField("player");
            player.setAccessible(true);
            player.set(event, ((ContribCraftPlayer)((CraftPlayer)event.getPlayer()).getHandle().getBukkitEntity()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
