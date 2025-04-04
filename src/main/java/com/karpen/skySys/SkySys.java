package com.karpen.skySys;

import com.karpen.skySys.commands.CheckCommand;
import com.karpen.skySys.commands.MainCompleter;
import com.karpen.skySys.commands.RestartCommand;
import com.karpen.skySys.commands.SizeCommand;
import com.karpen.skySys.listener.DeathListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public final class SkySys extends JavaPlugin {

    private CheckCommand checkCommand;
    private RestartCommand restartCommand;
    private SizeCommand sizeCommand;
    private MainCompleter completer;

    private DeathListener deathListener;

    @Override
    public void onEnable() {
        checkCommand = new CheckCommand(this);
        restartCommand = new RestartCommand(this);
        sizeCommand = new SizeCommand();
        completer = new MainCompleter();

        deathListener = new DeathListener();

        Objects.requireNonNull(getCommand("check")).setExecutor(checkCommand);
        Objects.requireNonNull(getCommand("check-stop")).setExecutor(checkCommand);
        Objects.requireNonNull(getCommand("size")).setExecutor(sizeCommand);
        Objects.requireNonNull(getCommand("vrestart")).setExecutor(restartCommand);

        Objects.requireNonNull(getCommand("check")).setTabCompleter(completer);
        Objects.requireNonNull(getCommand("check-stop")).setTabCompleter(completer);
        Objects.requireNonNull(getCommand("size")).setTabCompleter(completer);
        Objects.requireNonNull(getCommand("vrestart")).setTabCompleter(completer);

        Bukkit.getPluginManager().registerEvents(deathListener, this);

        getLogger().info("SkySys v1.0");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkySys v1.0");
    }
}
