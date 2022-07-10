package me.simondumalski.lightbulblegacy.combat.tasks;

import me.simondumalski.lightbulblegacy.Main;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class PvpCircleTask implements Runnable {

    private final Main plugin;

    /**
     * Constructor for the PvpCircleTask
     * @param plugin Instance of the main plugin class
     */
    public PvpCircleTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        //Loop through the UUIDs of players who have combat enabled
        for (UUID uuid : plugin.getCombatManager().getCombatEnabledPlayers()) {

            //Get the player with the matching UUID
            Player player = plugin.getServer().getPlayer(uuid);

            //Check if the player is valid
            if (player == null) {
                continue;
            }

            //Initialize a variable for the colour of the particles
            Color particleColor;

            //Set the particle color based on if the player is combat tagged
            if (plugin.getCombatManager().isTagged(player)) {
                particleColor = Color.fromRGB(199, 10, 0);
            } else {
                particleColor = Color.fromRGB(10, 199, 0);
            }

            //Get the player's location
            Location location = player.getLocation().clone();

            //Set the radius of the circle
            double radius = 0.65D;

            //Spawn the circle of particles
            for (int degree = 0; degree < 360; degree++) {
                double radians = Math.toRadians(degree);
                double x = radius * Math.cos(radians);
                double z = radius * Math.sin(radians);
                location.add(x,0,z);
                Objects.requireNonNull(location.getWorld()).spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(particleColor, 1));
                location.subtract(x,0,z);
            }

        }

    }

}
