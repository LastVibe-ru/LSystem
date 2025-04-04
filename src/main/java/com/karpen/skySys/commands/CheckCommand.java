package com.karpen.skySys.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CheckCommand implements CommandExecutor {

    private final Map<Player, ItemStack[]> inv = new HashMap<>();

    private JavaPlugin plugin;

    public CheckCommand(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("check")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "Вы не можете выполнить эту команду");

                return true;
            }

            Player player = (Player) commandSender;

            if (strings.length != 1) {
                player.sendMessage(ChatColor.RED + "Используйте /check <player name>");

                return true;
            }

            String targetName = strings[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "Игрок " + targetName + " не найден");

                return true;
            }

            startCheck(player, target);
            player.sendMessage(ChatColor.GREEN + "Вы начали проверку");

            return true;
        }

        if (command.getName().equalsIgnoreCase("check-stop")){
            if (!(commandSender instanceof Player)){
                commandSender.sendMessage(ChatColor.RED + "Эту команду может выполнять только игрок");

                return true;
            }

            Player player = (Player) commandSender;

            if (strings.length == 0){
                player.sendMessage(ChatColor.RED + "Используйте /check-stop <playername>");
                return true;
            }

            String targetName = strings[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null){
                player.sendMessage(ChatColor.RED + "Игрок " + targetName + " не найден");
                return true;
            }

            stopCheck(player, target);
            player.sendMessage(ChatColor.GREEN + "Проверка окончена");

            return true;
        }

        return false;
    }

    private void startCheck(Player sender, Player target){
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.LEVITATION, 99999, 1);
        sender.addPotionEffect(potionEffect);
        target.addPotionEffect(potionEffect);

        Location location = new Location(Bukkit.getWorld("world"), 0, -66, 0);
        sender.teleport(location);
        target.teleport(location);

        String title = ChatColor.DARK_AQUA + "Вас вызвали на проверку";
        String subtitle = ChatColor.RED + "Соблюдайте все указания администрации";

        target.sendTitle(title, subtitle, 20, 100, 20);
    }

    private void stopCheck(Player sender, Player target){
        inv.put(target, target.getInventory().getContents());
        target.setHealth(0);
        Bukkit.getScheduler().runTaskLater(plugin, () -> restoreInv(target), 20L);

        inv.put(sender, sender.getInventory().getContents());
        sender.setHealth(0);
        Bukkit.getScheduler().runTaskLater(plugin, () -> restoreInv(target), 20L);
    }

    private void restoreInv(Player player){
        if (inv.containsKey(player)){
            player.getInventory().setContents(inv.get(player));
        }
    }
}
