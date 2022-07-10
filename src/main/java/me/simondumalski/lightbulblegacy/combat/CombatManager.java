package me.simondumalski.lightbulblegacy.combat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.simondumalski.lightbulblegacy.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CombatManager {

    private final Main plugin;
    private final HashMap<UUID, Integer> taggedPlayers = new HashMap<>();
    private final List<UUID> combatEnabledPlayers = new ArrayList<>();
    private final List<UUID> combatLoggedPlayers;
    private final Cache<UUID, Long> toggleCooldown;

    /**
     * Constructor for the CombatManager
     * @param plugin Instance of the main plugin class
     */
    public CombatManager(Main plugin) {
        this.plugin = plugin;
        this.combatLoggedPlayers = plugin.getCombatDataConfig().loadData();
        this.toggleCooldown = CacheBuilder.newBuilder().expireAfterWrite(plugin.getConfig().getInt("combat.cooldown"), TimeUnit.SECONDS).build();
    }

    /* Combat Tagging */

    /**
     * Combat tags the provided player
     * @param player Player to combat tag
     */
    public void tagPlayer(Player player) {

        //Tag the player
        taggedPlayers.put(player.getUniqueId(), plugin.getConfig().getInt("combat.timer"));

        //Disable the player's PlayerParticles
        if (plugin.getPpAPI() != null) {
            plugin.getPpAPI().togglePlayerParticleVisibility(player, true);
        }

        //Disable the player's flight
        player.setFlying(false);
        player.setAllowFlight(false);

    }

    /**
     * Un-combat tags the provided player
     * @param player Player to un-combat tag
     */
    public void untagPlayer(Player player) {

        //Untag the player
        taggedPlayers.remove(player.getUniqueId());

        //Enable the player's PlayerParticles
        if (plugin.getPpAPI() != null) {
            plugin.getPpAPI().togglePlayerParticleVisibility(player, false);
        }

    }

    /**
     * Returns true/false if the provided player is combat tagged
     * @param player Player to check
     * @return True/false
     */
    public boolean isTagged(Player player) {
        return taggedPlayers.containsKey(player.getUniqueId());
    }

    /**
     * Returns the list of tagged players
     * @return List of player UUIDs
     */
    public HashMap<UUID, Integer> getTaggedPlayers() {
        return taggedPlayers;
    }

    /* Combat Logging */

    /**
     * Adds the provided player to the list of CombatLogged players
     * @param player Player to add
     */
    public void addCombatLoggedPlayer(Player player) {
        combatLoggedPlayers.add(player.getUniqueId());
    }

    /**
     * Removes the provided player from the list of CombatLogged players
     * @param player Player to remove
     */
    public void removeCombatLoggedPlayer(Player player) {
        combatLoggedPlayers.remove(player.getUniqueId());
    }

    /**
     * Returns true/false if the provided player is CombatLogged
     * @param player Player to check
     * @return True/false
     */
    public boolean isCombatLogged(Player player) {
        return combatLoggedPlayers.contains(player.getUniqueId());
    }

    /**
     * Returns the list of players who are combat logged
     * @return List of player UUIDs
     */
    public List<UUID> getCombatLoggedPlayers() {
        return combatLoggedPlayers;
    }

    /* PvP Toggling */

    /**
     * Enables pvp for the provided player
     * @param player Player to enable pvp for
     */
    public void enablePvp(Player player) {
        combatEnabledPlayers.add(player.getUniqueId());
    }

    /**
     * Disables pvp for the provided player
     * @param player Player to disable pvp for
     */
    public void disablePvp(Player player) {
        combatEnabledPlayers.remove(player.getUniqueId());
    }

    /**
     * Returns true/false if the provided player has pvp enabled
     * @param player Player to check
     * @return True/false
     */
    public boolean isPvpEnabled(Player player) {
        return combatEnabledPlayers.contains(player.getUniqueId());
    }

    /**
     * Returns the list of players who have pvp enabled
     * @return List of player UUIDs
     */
    public List<UUID> getCombatEnabledPlayers() {
        return combatEnabledPlayers;
    }

    /* Toggling Cooldown */

    /**
     * Adds the player to the cooldown map
     * @param player Player to add
     */
    public void addCooldown(Player player) {
        toggleCooldown.asMap().put(player.getUniqueId(), System.currentTimeMillis() + (plugin.getConfig().getInt("combat.cooldown") * 1000L));
    }

    /**
     * Removes the provided player's pvp toggling cooldown
     * @param player Player to remove the cooldown for
     */
    public void removeCooldown(Player player) {
        toggleCooldown.asMap().remove(player.getUniqueId());
    }

    /**
     * Returns true/false if the provided player is in the cooldown period
     * @param player Player to check
     * @return True/false
     */
    public boolean inCooldown(Player player) {
        return toggleCooldown.asMap().containsKey(player.getUniqueId());
    }

}
