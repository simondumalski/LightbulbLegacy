package me.simondumalski.lightbulblegacy.combat.listeners;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.messaging.MessageManager;
import me.simondumalski.lightbulblegacy.messaging.enums.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    private final Main plugin;

    /**
     * Constructor for the CommandListener
     * @param plugin Instance of the main plugin class
     */
    public CommandListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {

        //Get the player who is sending a command
        Player player = e.getPlayer();

        //Check if the player is combat tagged
        if (!plugin.getCombatManager().isTagged(player)) {
            return;
        }

        //Check if the player is exempt from command blocking
        if (player.hasPermission("lightbulblegacy.bypass-commands")) {
            return;
        }

        //Get the command the player is trying to send
        String command = e.getMessage();

        //Get the list of disabled commands from the config
        List<String> disabledCommands = plugin.getConfig().getStringList("plugin.combat.disabled-commands");

        //Check if the command being sent is in the list of disabled commands, and cancel it if it is
        for (String disabledCommand : disabledCommands) {
            if (disabledCommand.equalsIgnoreCase(command)) {
                e.setCancelled(true);
                MessageManager.sendMessage(player, Message.IN_COMBAT, null);
                return;
            }
        }

    }

}
