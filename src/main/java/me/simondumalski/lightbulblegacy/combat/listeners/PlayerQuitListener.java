package me.simondumalski.lightbulblegacy.combat.listeners;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.combat.CombatManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final Main plugin;
    private final CombatManager combatManager;

    /**
     * Constructor for the PlayerQuitListener
     * @param plugin Instance of the main plugin class
     */
    public PlayerQuitListener(Main plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        //Get the player who quit
        Player player = e.getPlayer();

        //Check if the player had pvp enabled
        if (combatManager.isPvpEnabled(player)) {
            combatManager.disablePvp(player);
        }

        //Check if the player was combat tagged
        if (!combatManager.isTagged(player)) {
            return;
        }

        //Un-combat tag the player
        combatManager.untagPlayer(player);

        //Remove the player's pvp toggle cooldown
        combatManager.removeCooldown(player);

        //Add the player to the list of combat logged players
        combatManager.addCombatLoggedPlayer(player);

        //Check if the player is exempt from balance loss on combat log
        if (player.hasPermission("lightbulblegacy.bypass-quit")){
            return;
        }

        //Get the player's balance
        double balance = plugin.getEconomy().getBalance(player);

        //Check if the player's balance is above 0
        if (balance <= 0) {
            return;
        }

        //Get the percentage to take from the player's balance
        int percentage = plugin.getConfig().getInt("combat.balance-loss");

        //Check if the percentage set is less than 0
        if (percentage < 0) {
            percentage = 0;
        }

        //Check if the percentage set is greater than 100
        if (percentage > 100) {
            percentage = 100;
        }

        //Calculate the amount to take from the player's balance
        double balanceLoss = balance * (percentage / 100D);

        //Withdraw the amount from the player's balance
        plugin.getEconomy().withdrawPlayer(player, balanceLoss);

    }

}
