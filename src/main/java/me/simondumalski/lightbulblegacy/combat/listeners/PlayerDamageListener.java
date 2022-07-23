package me.simondumalski.lightbulblegacy.combat.listeners;

import me.simondumalski.lightbulblegacy.Main;
import me.simondumalski.lightbulblegacy.combat.CombatManager;
import me.simondumalski.lightbulblegacy.messaging.MessageManager;
import me.simondumalski.lightbulblegacy.messaging.enums.Message;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    private final Main plugin;
    private final CombatManager combatManager;

    /**
     * Constructor for the PlayerDamageListener
     * @param plugin Instance of the main plugin class
     */
    public PlayerDamageListener(Main plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

        //Get the entities involved in the event
        Entity attackerEntity = e.getDamager();
        Entity defenderEntity = e.getEntity();

        //Check if the defending entity is a player
        if (!(defenderEntity instanceof Player defender)) {
            return;
        }

        //Get a variable ready for the attacking player
        Player attacker;

        //Check if the attacking entity is a player
        if ((attackerEntity instanceof Player)) {
            attacker = (Player) attackerEntity;

        //Otherwise check if the entity is a projectile
        } else {

            switch (attackerEntity.getType()) {

                case ARROW -> {
                    Arrow arrow = (Arrow) attackerEntity;
                    if (arrow.getShooter() instanceof Player) {
                        attacker = (Player) arrow.getShooter();
                    } else {
                        return;
                    }
                }

                case TRIDENT -> {
                    Trident trident = (Trident) attackerEntity;
                    if (trident.getShooter() instanceof Player) {
                        attacker = (Player) trident.getShooter();
                    } else {
                        return;
                    }
                }

                case SNOWBALL -> {
                    Snowball snowball = (Snowball) attackerEntity;
                    if (snowball.getShooter() instanceof Player) {
                        attacker = (Player) snowball.getShooter();
                    } else {
                        return;
                    }
                }

                case EGG -> {
                    Egg egg = (Egg) attackerEntity;
                    if (egg.getShooter() instanceof Player) {
                        attacker = (Player) egg.getShooter();
                    } else {
                        return;
                    }
                }

                case FIREWORK -> {
                    Firework firework = (Firework) attackerEntity;
                    if (firework.getShooter() instanceof Player) {
                        attacker = (Player) firework.getShooter();
                    } else {
                        return;
                    }
                }

                default -> {
                    return;
                }

            }

        }

        //Check if the attacker has pvp enabled
        if (!combatManager.isPvpEnabled(attacker)) {
            e.setCancelled(true);
            MessageManager.sendMessage(attacker, Message.ATTACKER_PVP_DISABLED, null);
            return;
        }

        //Check if the defender has pvp enabled
        if (!combatManager.isPvpEnabled(defender)) {
            e.setCancelled(true);
            MessageManager.sendMessage(attacker, Message.DEFENDER_PVP_DISABLED, new String[]{defender.getName()});
            return;
        }

        //Check if the attacker is already combat tagged
        if (!combatManager.isTagged(attacker)) {
            MessageManager.sendMessage(attacker, Message.ENTERING_COMBAT, null);
        }

        //Check if the defender is already combat tagged
        if (!combatManager.isTagged(defender)) {
            MessageManager.sendMessage(defender, Message.ENTERING_COMBAT, null);
        }

        //Combat tag the attacker and defender
        combatManager.tagPlayer(attacker);
        combatManager.tagPlayer(defender);

    }

}
