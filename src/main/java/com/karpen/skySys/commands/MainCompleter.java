package com.karpen.skySys.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1){
            if (command.getName().equalsIgnoreCase("check") || command.getName().equalsIgnoreCase("check-stop")){
                for (var player : Bukkit.getOnlinePlayers()){
                    suggestions.add(player.getName());
                }
            }

            if (command.getName().equalsIgnoreCase("size")){
                suggestions.add("min");
                suggestions.add("normal");
                suggestions.add("big");
            }
        }

        return suggestions;
    }
}
