package me.simondumalski.lightbulblegacy.admin;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.messaging.MessageManager;
import me.simondumalski.lightbulblegacy.messaging.enums.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LightbulbLegacyCommand implements TabExecutor {

    private final Main plugin;

    /**
     * Constructor for the LightbulbLegacyCommand
     * @param plugin Instance of the main plugin class
     */
    public LightbulbLegacyCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

            //Check if the player sent any command arguments or the help message
            if (args == null || args.length < 1 || args[0].equalsIgnoreCase("help")) {
                MessageManager.sendMessage(sender, Message.ADMIN_HELP, null);
                return true;
            }

            //Check if the command is "reload"
            if (args[0].equalsIgnoreCase("reload")){

                //Check if the player has permission to use the command
                if (sender.hasPermission("lightbulblegacy.admin")) {
                    plugin.reload();
                    MessageManager.sendMessage(sender, Message.RELOAD, null);
                } else {
                    MessageManager.sendMessage(sender, Message.INSUFFICIENT_PERMISSIONS, null);
                }

                return true;
            }

            //Otherwise send the unknown command message
            MessageManager.sendMessage(sender, Message.UNKNOWN_COMMAND, null);

        } else {
            sender.sendMessage(ChatColor.RED + "Only players may use this command.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        //Argument 0: Possible Command Arguments
        if (args.length == 1) {

            //Add the possible command arguments to a list
            List<String> arguments = new ArrayList<>();
            arguments.add("reload");

            //Return the list of possible arguments based on the already typed characters
            return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
        }

        return null;
    }

}
