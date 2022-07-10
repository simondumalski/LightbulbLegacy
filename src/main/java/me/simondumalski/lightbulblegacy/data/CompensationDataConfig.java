package me.simondumalski.lightbulblegacy.data;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.messaging.ConsoleLogger;
import me.simondumalski.lightbulblegacy.messaging.enums.Log;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompensationDataConfig {

    private final Main plugin;
    private File file;
    private YamlConfiguration config;
    private final String FILE_NAME = "compensation-data.yml";

    /**
     * Constructor for the CompensationDataConfig
     * @param plugin Instance of the main plugin class
     */
    public CompensationDataConfig(Main plugin) {

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
     * Saves the list of compensated players to file
     */
    public void saveData() {

        try {

            //Get the list of players who have been compensated
            List<UUID> compensatedPlayers = plugin.getCompensationManager().getCompensatedPlayers();

            //Convert each UUID to a String
            List<String> uuidStrings = new ArrayList<>();
            for (UUID uuid : compensatedPlayers) {
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
     * Loads the list of compensated players from file
     */
    public List<UUID> loadData() {

        try {

            //Initialize a list for storing player UUIDs
            List<UUID> compensatedPlayers = new ArrayList<>();

            //Get the list of UUID Strings from file
            List<String> uuidStrings = config.getStringList("data");

            //Convert each UUID String to a UUID
            for (String uuidString : uuidStrings) {
                compensatedPlayers.add(UUID.fromString(uuidString));
            }

            //Log to console that the file was loaded
            ConsoleLogger.logToConsole(Log.FILE_LOADED, new String[]{FILE_NAME});

            //Return the list of compensated players
            return compensatedPlayers;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
