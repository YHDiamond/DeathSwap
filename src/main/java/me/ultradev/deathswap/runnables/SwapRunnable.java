package me.ultradev.deathswap.runnables;

import me.ultradev.deathswap.DeathSwapManager;
import me.ultradev.deathswap.Main;
import me.ultradev.deathswap.api.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SwapRunnable extends BukkitRunnable {

    int timer = 0;

    @Override
    public void run() {

        if(!DeathSwapManager.gameActive) {
            this.cancel();
            return;
        }

        timer++;
        if(timer >= 60) {

            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.hasPermission("deathswap.participate")) {
                    player.sendTitle(Main.toColor("§cSWAPPING IN 5 SECONDS!"), "", 0, 100, 0);
                }
            }

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), task -> {

                if(!DeathSwapManager.gameActive) return;
                HashMap<Player, Player> swaps = new HashMap<>();
                List<Player> players = new ArrayList<>(DeathSwapManager.players);

                for(Player player : new ArrayList<>(players)) {

                    Player swapPlayer = players.get(NumberUtil.getRandomBetween(0, players.size() - 1));
                    for(int i = 0; i < 1000; i++) {
                        if(player.getUniqueId().equals(swapPlayer.getUniqueId())) {
                            swapPlayer = players.get(NumberUtil.getRandomBetween(0, players.size() - 1));
                        } else break;
                    }

                    swaps.put(player, swapPlayer);
                    players.remove(swapPlayer);

                }

                HashMap<Player, Location> locations = new HashMap<>();
                for(Player player : players) {
                    locations.put(player, player.getLocation());
                }

                for(Player player : swaps.keySet()) {
                    player.teleport(locations.get(swaps.get(player)));
                    player.sendMessage("§aYou swapped with " + swaps.get(player).getName() + "!");
                }

                timer = 0;

            }, 100);

        }

    }

}
