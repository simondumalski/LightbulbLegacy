package me.simondumalski.lightbulblegacy.data;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.messaging.ConsoleLogger;
import me.simondumalski.lightbulblegacy.messaging.enums.Log;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CombatDataConfig {

    private final Main plugin;
    private File file;
    private YamlConfiguration config;
    private final String FILE_NAME = "combat-data.yml";

    /**
     * Constructor for the CombatDataConfig
     * @param plugin Instance of the main plugin class
     */
    public CombatDataConfig(Main plugin) {

        //Set the instance of the main plugin class
        this.plugin = plugin;

        //Initialize the config
        try {

            file = new File(plugin.getDataFolder(), FILE_NAME);
            config = YamlConfiguration.loadConfiguration(file);

            if (!config.isConfigurationSection("data")) {
                config.createSection("data");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Saves the combat data to file
     */
    public void saveData() {

        try {

            //Get the list of combat logged players
            List<UUID> combatLoggedPlayers = plugin.getCombatManager().getCombatLoggedPlayers();

            //Get a list ready for storing the converted player UUIDs
            List<String> uuidStrings = new ArrayList<>();

            //Convert the UUIDs to Strings
            for (UUID uuid : combatLoggedPlayers) {
                uuidStrings.add(uuid.toString());
            }

            //Save the list of UUIDs to file
            config.set("data", uuidStrings);

            //Save the file
            config.save(file);

            //Log to console that the file was saved
            ConsoleLogger.logToConsole(Log.FILE_SAVED, new String[]{FILE_NAME});

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Loads the combat data from file
     */
    public List<UUID> loadData() {

        try {

            //Initialize a list for storing player UUIDs
            List<UUID> combatLoggedPlayers = new ArrayList<>();

            //Get the list of UUID strings from file
            List<String> uuidStrings = config.getStringList("data");

            //Convert each String to a UUID
            for (String uuidString : uuidStrings) {
                combatLoggedPlayers.add(UUID.fromString(uuidString));
            }

            //Log to console that the file was loaded
            ConsoleLogger.logToConsole(Log.FILE_LOADED, new String[]{FILE_NAME});

            //Return the list of combat logged players
            return combatLoggedPlayers;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
