package me.simondumalski.lightbulblegacy.compensate;

import me.simondumalski.lightbulblegacy.Main;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CompensationManager {

    private final Main plugin;
    private List<UUID> compensatedPlayers;

    /**
     * Constructor for the CompensationManager
     * @param plugin Instance of the main plugin class
     */
    public CompensationManager(Main plugin) {
        this.plugin = plugin;
        this.compensatedPlayers = plugin.getCompensationDataConfig().loadData();
    }

    /**
     * Compensates the provided player
     * @param player Player to compensate
     */
    public void compensatePlayer(Player player) {

        //Get the console command sender
        ConsoleCommandSender consoleCommandSender = plugin.getServer().getConsoleSender();

        //Get the list of commands to run from the config.yml
        List<String> commands = plugin.getConfig().getStringList("compensate");

        //Run each command in the list
        for (String command : commands) {

            //Replace the %player% prefix
            if (command.contains("%player%")) {
                command = command.replace("%player%", player.getName());
            }

            //Dispatch the command
            plugin.getServer().dispatchCommand(consoleCommandSender, command);

        }

        //Add the player to the list of compensated players
        compensatedPlayers.add(player.getUniqueId());

    }

    /**
     * Returns true/false if the provided player has already been compensated
     * @param player Player to check
     * @return True/false
     */
    public boolean isCompensated(Player player) {
        return compensatedPlayers.contains(player.getUniqueId());
    }

    /**
     * Returns the list of players who have been compensated
     * @return List of player UUIDS
     */
    public List<UUID> getCompensatedPlayers() {
        return compensatedPlayers;
    }

    /**
     * Sets the list of players who have been compensated
     * @param compensatedPlayers List of player UUIDs
     */
    public void setCompensatedPlayers(List<UUID> compensatedPlayers) {
        this.compensatedPlayers = compensatedPlayers;
    }

}
