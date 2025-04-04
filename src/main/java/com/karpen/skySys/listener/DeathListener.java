package com.karpen.skySys.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if (event.getEntity().getKiller() == null){
            return;
        }

        Player player = (Player) event.getEntity();
        Player killer = (Player) event.getEntity().getKiller();

        assert killer != null;
        ItemStack item = killer.getInventory().getItemInMainHand();

        if (item.equals(Material.WOODEN_AXE) || item.equals(Material.STONE_AXE) || item.equals(Material.IRON_AXE) || item.equals(Material.GOLDEN_AXE) || item.equals(Material.DIAMOND_AXE) || item.equals(Material.NETHERITE_AXE)){
            Location location = player.getLocation();

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            if (meta != null){
                meta.setOwningPlayer(player);
                head.setItemMeta(meta);
            }

            Objects.requireNonNull(location.getWorld()).dropItem(location, head);
        }
    }
}
