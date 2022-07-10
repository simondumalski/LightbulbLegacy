package me.simondumalski.lightbulblegacy.combat.commands;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.combat.CombatManager;
import me.simondumalski.lightbulblegacy.messaging.MessageManager;
import me.simondumalski.lightbulblegacy.messaging.enums.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PvpCommand implements TabExecutor {

    private final Main plugin;
    private final CombatManager combatManager;

    /**
     * Constructor for the PvpCommand
     * @param plugin Instance of the main plugin class
     */
    public PvpCommand(Main plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            //Check if the player is sending any arguments or the help command
            if (args == null || args.length < 1 || args[0].equalsIgnoreCase("help")) {
                MessageManager.sendMessage(player, Message.COMPENSATE_HELP, null);
                return true;
            }

            //Check if the player is currently in combat
            if (combatManager.isTagged(player)) {
                MessageManager.sendMessage(player, Message.IN_COMBAT, null);
                return true;
            }

            //Check if the player is in the cooldown period
            if (combatManager.inCooldown(player)) {
                MessageManager.sendMessage(player, Message.IN_COOLDOWN, null);
                return true;
            }

            switch (args[0].toLowerCase()) {

                case "toggle" -> {

                    if (combatManager.isPvpEnabled(player)) {
                        combatManager.disablePvp(player);
                        MessageManager.sendMessage(player, Message.PVP_DISABLED, null);
                    } else {
                        combatManager.enablePvp(player);
                        MessageManager.sendMessage(player, Message.PVP_ENABLED, null);
                    }

                }

                case "on", "enable" -> {
                    combatManager.enablePvp(player);
                    MessageManager.sendMessage(player, Message.PVP_ENABLED, null);
                }

                case "off", "disable" -> {
                    combatManager.disablePvp(player);
                    MessageManager.sendMessage(player, Message.PVP_DISABLED, null);
                }

                default -> {
                    MessageManager.sendMessage(player, Message.UNKNOWN_COMMAND, null);
                    return true;
                }

            }

            //Check if the player is exempt from the cooldown period
            if (player.hasPermission("lightbulblegacy.bypass-cooldown")) {
                return true;
            }

            //Put the player in the cooldown
            combatManager.addCooldown(player);

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
            arguments.add("toggle");
            arguments.add("on");
            arguments.add("enable");
            arguments.add("off");
            arguments.add("disable");

            //Return the list of possible arguments based on the already typed characters
            return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
        }

        return null;
    }

}
