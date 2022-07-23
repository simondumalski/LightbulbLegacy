package me.simondumalski.lightbulblegacy.combat.tasks;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.combat.CombatManager;
import me.simondumalski.lightbulblegacy.messaging.MessageManager;
import me.simondumalski.lightbulblegacy.messaging.enums.Message;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TagTimerTask implements Runnable {

    private final Main plugin;
    private final CombatManager combatManager;

    /**
     * Constructor for the TagTimerTask
     * @param plugin Instance of the main plugin class
     */
    public TagTimerTask(Main plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
    }

    @Override
    public void run() {

        try {

            //Loop through each tagged player
            for (UUID uuid : combatManager.getTaggedPlayers().keySet()) {

                //Get the tagged time and the player
                int taggedTime = combatManager.getTaggedPlayers().get(uuid);
                Player player = plugin.getServer().getPlayer(uuid);

                //If the player is null, skip it
                if (player == null) {
                    continue;
                }

                //If the tagged time is less than 1, untag the player
                if (taggedTime < 1) {
                    combatManager.untagPlayer(player);
                    MessageManager.sendMessage(player, Message.LEAVING_COMBAT, null);
                    continue;
                }

                //Decrement the time remaining in the combat tag
                combatManager.getTaggedPlayers().replace(uuid, (taggedTime - 1));


            }

        } catch (Exception ignored) {

        }

    }

}
