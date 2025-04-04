package com.karpen.skySys.commands;

import com.maximde.entitysize.EntityModifierService;
import com.maximde.entitysize.EntitySize;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SizeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 1){
            commandSender.sendMessage(ChatColor.RED + "Используйте /size < big | normal | min >");

            return true;
        }

        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок");

            return true;
        }

        Player player = (Player) commandSender;

        Optional<EntityModifierService> modifierService = EntitySize.getSizeService();

        switch (strings[0].toLowerCase()){
            case "min" -> {
                modifierService.get().setSize(player, 0.88);

                return true;
            }
            case "normal" -> {
                modifierService.get().resetSize(player);

                return true;
            }
            case "big" -> {
                modifierService.get().setSize(player, 1.2);

                return true;
            }
            default -> {
                player.sendMessage(ChatColor.RED + "Неизвестная команда");

                return true;
            }
        }
    }
}
