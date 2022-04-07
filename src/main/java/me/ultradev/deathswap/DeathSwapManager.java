package me.ultradev.deathswap;

import me.ultradev.deathswap.api.util.NumberUtil;
import me.ultradev.deathswap.runnables.SwapRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeathSwapManager {

    public static boolean gameActive = false;
    public static List<Player> players = new ArrayList<>();

    public static void startGame() {

        Bukkit.broadcastMessage("§aA game of DeathSwap is starting in 5 seconds!");
        gameActive = true;
        players.clear();
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission("deathswap.participate")) players.add(player);
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), task -> {

            if(!gameActive) return;
            Bukkit.broadcastMessage("§aTeleporting players...");

            List<Location> locations = new ArrayList<>();
            for(Player player : players) {

                Location playerLocation = new Location(player.getWorld(),
                        NumberUtil.getRandomBetween(-100000, 100000),
                        0,
                        NumberUtil.getRandomBetween(-100000, 100000));

                for(int i = 0; i < 1000; i++) {
                    if(!playerLocation.getBlock().getType().equals(Material.AIR)) {
                        playerLocation.add(0, 2, 0);
                    } else break;
                }

                for(int i = 0; i < 1000; i++) {
                    for(Location checkLocation : locations) {
                        if(checkLocation.distance(playerLocation) < 5000) {
                            playerLocation = new Location(player.getWorld(),
                                    NumberUtil.getRandomBetween(-100000, 100000),
                                    playerLocation.getY(),
                                    NumberUtil.getRandomBetween(-100000, 100000));
                        } else break;
                    }
                }

                locations.add(playerLocation);
                player.teleport(playerLocation);

            }

            Bukkit.broadcastMessage("§aDeathSwap has started!");
            new SwapRunnable().runTaskTimer(Main.getInstance(), 0, 20);

        }, 100);

    }

    public static void checkGameEnd() {

        if(gameActive && players.size() == 1) {

            Bukkit.broadcast("§6§l" + players.get(0).getName() + " won the game! Congratulations!", "deathswap.participate");
            gameActive = false;
            players.clear();
            DeathSwapManager.checkGameEnd();

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.hasPermission("deathswap.participate")) {
                    player.teleport(player.getWorld().getSpawnLocation());
                }
            }

        }

    }

}
