package com.karpen.skySys.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class RestartCommand implements CommandExecutor {

    private JavaPlugin plugin;

    public RestartCommand(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        int countdownTime = 60;
        BossBar bar = Bukkit.createBossBar("Рестарт через:", BarColor.WHITE, BarStyle.SOLID);
        bar.setVisible(true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            bar.addPlayer(player);
        }

        new BukkitRunnable() {
            int remainingTime = countdownTime;

            @Override
            public void run() {
                if (remainingTime > 0) {
                    bar.setProgress((double) remainingTime / countdownTime);
                    bar.setTitle("Рестарт через: " + remainingTime);

                    if (remainingTime == 30) {
                        Bukkit.broadcastMessage(ChatColor.WHITE + "Рестарт через 30 секунд");
                    } else if (remainingTime == 10) {
                        Bukkit.broadcastMessage(ChatColor.WHITE + "Рестарт через 10 секунд");
                    } else if (remainingTime == 5) {
                        Bukkit.broadcastMessage(ChatColor.WHITE + "Рестарт через 5 секунд");
                    } else if (remainingTime == 3) {
                        playSoundAllPlayers();
                        Bukkit.broadcastMessage(ChatColor.WHITE + "Рестарт через 3 секунды");
                    } else if (remainingTime == 2) {
                        playSoundAllPlayers();
                        Bukkit.broadcastMessage(ChatColor.WHITE + "Рестарт через 2 секунды");
                    } else if (remainingTime == 1) {
                        playSoundAllPlayers();
                        Bukkit.broadcastMessage(ChatColor.WHITE + "Рестарт через 1 секунду");
                    }

                    remainingTime--;
                } else {
                    Bukkit.broadcastMessage(ChatColor.WHITE + "Рестарт");
                    bar.setVisible(false);
                    bar.removeAll();

                    Bukkit.getServer().shutdown();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);

        return true;
    }

    private void playSoundAllPlayers(){
        for (Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
    }
}
