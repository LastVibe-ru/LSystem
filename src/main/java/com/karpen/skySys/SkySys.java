package com.karpen.skySys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public final class SkySys extends JavaPlugin {

    private final Map<Player, ItemStack[]> inv = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("SkySys v1.0");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkySys v1.0");
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
        Bukkit.getScheduler().runTaskLater(this, () -> restoreInv(target), 20L);

        inv.put(sender, sender.getInventory().getContents());
        sender.setHealth(0);
        Bukkit.getScheduler().runTaskLater(this, () -> restoreInv(target), 20L);
    }

    private void restoreInv(Player player){
        if (inv.containsKey(player)){
            player.getInventory().setContents(inv.get(player));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("check")){
            if (!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED + "Эту команду может выполнять только игрок");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Используйте /check <playername>");
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null){
                player.sendMessage(ChatColor.RED + "Игрок " + targetName + " не найден");
                return true;
            }

            startCheck(player, target);
            player.sendMessage(ChatColor.GREEN + "Вы начали проверку");

            return true;
        }

        if (command.getName().equalsIgnoreCase("check-stop")){
            if (!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED + "Эту команду может выполнять только игрок");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0){
                player.sendMessage(ChatColor.RED + "Используйте /check-stop <playername>");
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null){
                player.sendMessage(ChatColor.RED + "Игрок " + targetName + " не найден");
                return true;
            }

            stopCheck(player, target);
            player.sendMessage(ChatColor.GREEN + "Проверка окончена");

            return true;
        }

        if (command.getName().equalsIgnoreCase("website")){
            if (!(sender instanceof  Player)){
                sender.sendMessage(ChatColor.RED + "Эту команду может выполнять только игрок");
                return true;
            }

            Player player = (Player) sender;
            player.sendMessage("§x§3§3§A§5§D§3Н§x§3§1§A§2§C§Eа§x§2§F§9§F§C§Aш §x§2§D§9§C§C§5с§x§2§B§9§A§C§0а§x§2§9§9§7§B§Bй§x§2§6§9§4§B§7т §x§2§4§9§1§B§2h§x§2§2§8§E§A§Dt§x§2§0§8§B§A§8t§x§1§E§8§8§A§4p§x§1§C§8§5§9§Fs§x§1§A§8§3§9§A:§x§1§8§8§0§9§5/§x§1§6§7§D§9§1/§x§1§4§7§A§8§Cs§x§1§2§7§7§8§7k§x§1§0§7§4§8§2y§x§0§D§7§1§7§Eu§x§0§B§6§E§7§9p§x§0§9§6§C§7§4.§x§0§7§6§9§6§Fs§x§0§5§6§6§6§Bu§x§0§3§6§3§6§6/");

            return true;
        }
        return false;
    }
}
