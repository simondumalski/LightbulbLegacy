package me.simondumalski.lightbulblegacy.combat.listeners;

import me.simondumalski.lightbulblegacy.Main;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Main plugin;

    /**
     * Constructor for the PlayerJoinListener
     * @param plugin Instance of the main plugin class
     */
    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        //Get the player who joined
        Player player = e.getPlayer();

        //Check if the player combat logged
        if (!plugin.getCombatManager().isCombatLogged(player)) {
            return;
        }

        //Remove the player from the list of combat logged players
        plugin.getCombatManager().removeCombatLoggedPlayer(player);

        //Teleport the player to /spawn
        ConsoleCommandSender consoleCommandSender = plugin.getServer().getConsoleSender();
        plugin.getServer().dispatchCommand(consoleCommandSender, "spawn " + player.getName());

    }

}
