package me.simondumalski.lightbulblegacy.messaging;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.messaging.enums.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {

    private static Main plugin;

    /**
     * Sends a message from the config.yml to the specified CommandSender
     * @param sender CommandSender to send the message to
     * @param configValue Message to send
     * @param args Message arguments, send null if none
     */
    public static void sendMessage(CommandSender sender, Message configValue, String[] args) {

        //Get the message to send
        String message = plugin.getConfig().getString(configValue.getMessage());

        //Check if the message is valid
        if (message == null) {
            sender.sendMessage(configValue.getMessage());
            return;
        }

        //Get the plugin prefix
        String prefix = plugin.getConfig().getString("messages.prefix");

        //Replace the %prefix% placeholder
        if (prefix != null && message.contains("%prefix%")) {
            message = message.replace("%prefix%", prefix);
        }

        //Replace the %args#% placeholders
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String placeholder = "%args" + i + "%";
                if (message.contains(placeholder)) {
                    message = message.replace(placeholder, args[i]);
                }
            }
        }

        //Translate the color codes
        message = ChatColor.translateAlternateColorCodes('&', message);

        //Send the message to the CommandSender
        sender.sendMessage(message);

    }

    /**
     * Sets the instance of the main plugin class
     * @param plugin Instance of the main plugin class
     */
    public static void setPlugin(Main plugin) {
        MessageManager.plugin = plugin;
    }

}
