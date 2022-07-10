package me.simondumalski.lightbulblegacy.messaging;

import me.simondumalski.lightbulblegacy.messaging.enums.Log;
import org.bukkit.ChatColor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleLogger {

    private static Logger logger;

    /**
     * Logs a message to console
     * @param log Message to log
     */
    public static void logToConsole(Log log, String[] args) {

        //Get the message to log
        String message = log.getMessage();

        //Get the level of the log
        Level level = log.getLevel();

        //Replace the %args#% placeholders
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String placeholder = "%args" + i + "%";
                if (message.contains(placeholder)) {
                    message = message.replace(placeholder, args[i]);
                }
            }
        }

        //Log the message to console
        logger.log(level, message);

    }

    /**
     * Sets the instance of the Logger
     * @param logger Instance of the plugin Logger
     */
    public static void setLogger(Logger logger) {
        ConsoleLogger.logger = logger;
    }

}
