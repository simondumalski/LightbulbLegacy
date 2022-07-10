package me.simondumalski.lightbulblegacy.combat.listeners;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.combat.CombatManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final Main plugin;
    private final CombatManager combatManager;

    /**
     * Constructor for the PlayerDeathListener
     * @param plugin Instance of the main plugin class
     */
    public PlayerDeathListener(Main plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        //Get the player who died
        Player player = e.getEntity();

        //Check if the player was combat tagged
        if (!combatManager.isTagged(player)) {
            return;
        }

        //Untag the player who died
        combatManager.untagPlayer(player);

        //Remove the player's pvp toggle cooldown
        combatManager.removeCooldown(player);

        //Teleport the player who died to /spawn
        ConsoleCommandSender consoleCommandSender = plugin.getServer().getConsoleSender();
        plugin.getServer().dispatchCommand(consoleCommandSender, "spawn " + player.getName());

    }

}
