package me.simondumalski.lightbulblegacy.compensate.commands;

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

public class CompensateCommand implements TabExecutor {

    private final Main plugin;

    public CompensateCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

            //Check if the player sent any command arguments or the help message
            if (args == null || args.length < 1 || args[0].equalsIgnoreCase("help")) {
                MessageManager.sendMessage(sender, Message.COMPENSATE_HELP, null);
                return true;
            }

            //Check if the player has permission to use the command
            if (!sender.hasPermission("lightbulblegacy.compensate")) {
                MessageManager.sendMessage(sender, Message.INSUFFICIENT_PERMISSIONS, null);
                return true;
            }

            //Get the target player
            Player target = plugin.getServer().getPlayer(args[0]);

            //Check if the target player is valid
            if (target == null) {
                MessageManager.sendMessage(sender, Message.INVALID_PLAYER, null);
                return true;
            }

            //Check if the target player has already been compensated
            if (plugin.getCompensationManager().isCompensated(target)) {
                MessageManager.sendMessage(sender, Message.ALREADY_COMPENSATED, null);
                return true;
            }

            //Compensate the target player
            plugin.getCompensationManager().compensatePlayer(target);

            //Send the CommandSender and the target player a success message
            MessageManager.sendMessage(sender, Message.COMPENSATION_SENT, new String[]{target.getName()});
            MessageManager.sendMessage(target, Message.COMPENSATION_RECEIVED, new String[]{sender.getName()});

        } else {
            sender.sendMessage(ChatColor.RED + "Only players may use this command.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        //Argument 0: Player name
        if (args.length == 1) {

            //Add the username of each online player to a list
            List<String> arguments = new ArrayList<>();

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                arguments.add(player.getName());
            }

            //Return the list of player names that match the typed characters
            return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
        }

        return null;
    }

}
