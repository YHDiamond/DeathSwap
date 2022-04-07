package me.ultradev.deathswap.commands;

import me.ultradev.deathswap.DeathSwapManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DeathSwapStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if(!sender.hasPermission("deathswap.commands.ds-start")) {
            sender.sendMessage("&cYou don't have permission to do this!");
            return false;
        }

        if(DeathSwapManager.gameActive) {
            sender.sendMessage("&cThere is already a game of DeathSwap active!");
            return false;
        }

        DeathSwapManager.startGame();
        return true;

    }

}
