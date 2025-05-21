package com.karpen.skySys.listener;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReadyListener extends ListenerAdapter {

    private JavaPlugin plugin;

    public ReadyListener(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        plugin.getLogger().info("Discord bot initialize");
    }
}
