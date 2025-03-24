package com.karpen.skySys;

import com.maximde.entitysize.EntityModifierService;
import com.maximde.entitysize.EntitySize;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public final class SkySys extends JavaPlugin implements TabCompleter {

    private final Map<Player, ItemStack[]> inv = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("SkySys v1.0");

        Objects.requireNonNull(getCommand("size")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("check")).setTabCompleter(this);
        Objects.requireNonNull(getCommand("check-stop")).setTabCompleter(this);
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

        if (command.getName().equalsIgnoreCase("size")){
            if (args.length != 1){
                sender.sendMessage(ChatColor.RED + "Используйте /size < big | normal | min >");

                return true;
            }

            if (!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED + "Эту команду может юзать только игрок");

                return true;
            }

            Player player = (Player) sender;

            Optional<EntityModifierService> modifierService = EntitySize.getSizeService();

            if (args[0].equalsIgnoreCase("min")){
                modifierService.get().setSize(player, 0.88);

                return true;
            }

            if (args[0].equalsIgnoreCase("normal")){
                modifierService.get().resetSize(player);

                return true;
            }

            if (args[0].equalsIgnoreCase("big")){
                modifierService.get().setSize(player, 1.2);

                return true;
            }
        }

        if (command.getName().equalsIgnoreCase("ncolor")){
            if (!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED + "Эту команду может юзать только игрок");

                return true;
            }

            Player player = (Player) sender;


        }

        return false;
    }

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
