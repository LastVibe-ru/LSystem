package com.karpen.skySys.managers;

import com.karpen.skySys.listener.ReadyListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class StatusManager {

    private final JavaPlugin plugin;
    private final String token;
    private Configuration config;
    private long channelId;
    private long messageId;
    private boolean messageExists;
    private JDA jda;

    public StatusManager(JavaPlugin plugin, Configuration config) {
        this.plugin = plugin;
        this.config = config;

        this.token = config.getString("discord.token");
        this.channelId = config.getLong("discord.channelId");

        if (token == null || token.isEmpty()) {
            plugin.getLogger().severe("Unknown token");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        this.messageId = config.getLong("discord.messageId", 0);
        if (this.messageId != 0) {
            this.messageExists = true;
        }

        try {
            initializeBot();
        } catch (LoginException e){
            e.printStackTrace();
        }
    }

    private void initializeBot() throws LoginException {
        jda = JDABuilder.createDefault(token)
                .build();

        jda.addEventListener(new ReadyListener(plugin));

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startUpdatingTask();
    }

    private void startUpdatingTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateServerStatus();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20 * 60);
    }

    private void updateServerStatus() {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            plugin.getLogger().severe("Discord channel " + channelId + " not found");
            return;
        }

        int online = Bukkit.getOnlinePlayers().size();
        int maxPlayers = Bukkit.getMaxPlayers();
        // TODO: Add tps score
        int tps = 20;

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Игровой сервер")
                .setColor(Color.GREEN)
                .addField("Онлайн", online + "/" + maxPlayers, true)
                .addField("TPS", String.valueOf(tps), true)
                .addField("Версия", Bukkit.getVersion(), false)
                .setFooter("Последние обновление")
                .setTimestamp(Instant.now());

        if (messageExists) {
            channel.retrieveMessageById(messageId).queue(
                    message -> message.editMessageEmbeds(embed.build()).queue(),
                    error -> {
                        sendNewStatusMessage(channel, embed);
                    }
            );
        } else {
            sendNewStatusMessage(channel, embed);
        }
    }

    private void sendNewStatusMessage(TextChannel channel, EmbedBuilder embed) {
        channel.sendMessageEmbeds(embed.build()).queue(message -> {
            this.messageId = message.getIdLong();
            this.messageExists = true;

            config.set("discord.messageId", messageId);
            plugin.saveConfig();
            config = plugin.getConfig();
        });
    }

    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
            try {
                if (!jda.awaitShutdown(10, TimeUnit.SECONDS)) {
                    jda.shutdownNow();
                }
            } catch (InterruptedException e) {
                jda.shutdownNow();
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
