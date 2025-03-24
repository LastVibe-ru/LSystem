package com.karpen.skySys;

import com.maximde.entitysize.EntityModifierService;
import com.maximde.entitysize.EntitySize;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class SkySys extends JavaPlugin implements TabCompleter, Listener {

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

    private void playSoundAllPlayers(){
        for (Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
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

        if (command.getName().equalsIgnoreCase("vrestart")) {
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
            }.runTaskTimer(this, 0, 20);

            return true;
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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if (event.getEntity().getKiller() == null){
            return;
        }

        Player player = (Player) event.getEntity();
        Player killer = (Player) event.getEntity().getKiller();

        assert killer != null;
        ItemStack item = killer.getInventory().getItemInMainHand();

        if (item.equals(Material.WOODEN_AXE) || item.equals(Material.STONE_AXE) || item.equals(Material.IRON_AXE) || item.equals(Material.GOLDEN_AXE) || item.equals(Material.DIAMOND_AXE) || item.equals(Material.NETHERITE_AXE)){
            Location location = player.getLocation();

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            if (meta != null){
                meta.setOwningPlayer(player);
                head.setItemMeta(meta);
            }

            Objects.requireNonNull(location.getWorld()).dropItem(location, head);
        }
    }
}
