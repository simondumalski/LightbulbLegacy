package me.simondumalski.lightbulblegacy.combat.listeners;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.combat.CombatManager;
import me.simondumalski.lightbulblegacy.messaging.MessageManager;
import me.simondumalski.lightbulblegacy.messaging.enums.Message;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.Collection;

public class PotionSplashListener implements Listener {

    private final Main plugin;
    private final CombatManager combatManager;

    /**
     * Constructor for the PotionSplashListener
     * @param plugin Instance of the main plugin class
     */
    public PotionSplashListener(Main plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {

        //Get the potion that was thrown
        ThrownPotion potion = e.getEntity();

        //Check if the entity that threw the potion is a player
        if (!(potion.getShooter() instanceof Player attacker)) {
            return;
        }

        //Get the list of Entities affected by the potion
        Collection<LivingEntity> livingEntities = e.getAffectedEntities();

        boolean attackerMessaged = false;

        //Check if any of the affected entities are players
        for (LivingEntity livingEntity : livingEntities) {

            //Check if the entity is a player
            if (!(livingEntity instanceof Player defender)) {
                continue;
            }

            //Check if the attacker has pvp enabled
            if (!combatManager.isPvpEnabled(attacker)) {
                e.setIntensity(defender, 0);
                if (!attackerMessaged) {
                    MessageManager.sendMessage(attacker, Message.ATTACKER_PVP_DISABLED, null);
                }
                continue;
            }

            //Check if the defender has pvp enabled
            if (!combatManager.isPvpEnabled(defender)) {
                e.setIntensity(defender, 0);
                if (!attackerMessaged) {
                    MessageManager.sendMessage(attacker, Message.DEFENDER_PVP_DISABLED, new String[]{defender.getName()});
                }
                continue;
            }

            //Check if the attacker is already combat tagged
            if (!combatManager.isTagged(attacker)) {
                MessageManager.sendMessage(attacker, Message.ENTERING_COMBAT, null);
            }

            //Check if the defender is already combat tagged
            if (!combatManager.isTagged(defender)) {
                MessageManager.sendMessage(defender, Message.ENTERING_COMBAT, null);
            }

            //Tag the attacker and defender
            combatManager.isTagged(attacker);
            combatManager.isTagged(defender);

        }

    }

}
