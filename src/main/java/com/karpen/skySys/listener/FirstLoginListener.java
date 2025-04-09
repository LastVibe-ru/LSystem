package com.karpen.skySys.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class FirstLoginListener implements Listener {

    // This listener fix bug with sync database
    // If you don't using MySQL-Sync remove this

    @EventHandler
    public void onFirstJoin(PlayerLoginEvent event){
        if (event.getPlayer().hasPlayedBefore()){
            event.getPlayer().kickPlayer(ChatColor.WHITE + "Ваши данные синхронизированы, пожалуйста перезайдите");
        }
    }
}
