package me.simondumalski.lightbulblegacy;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import me.simondumalski.lightbulblegacy.admin.LightbulbLegacyCommand;
import me.simondumalski.lightbulblegacy.combat.CombatManager;
import me.simondumalski.lightbulblegacy.combat.commands.PvpCommand;
import me.simondumalski.lightbulblegacy.combat.listeners.*;
import me.simondumalski.lightbulblegacy.combat.tasks.PvpCircleTask;
import me.simondumalski.lightbulblegacy.combat.tasks.TagTimerTask;
import me.simondumalski.lightbulblegacy.compensate.CompensationManager;
import me.simondumalski.lightbulblegacy.compensate.commands.CompensateCommand;
import me.simondumalski.lightbulblegacy.data.CombatDataConfig;
import me.simondumalski.lightbulblegacy.data.CompensationDataConfig;
import me.simondumalski.lightbulblegacy.data.DataSaveTask;
import me.simondumalski.lightbulblegacy.messaging.ConsoleLogger;
import me.simondumalski.lightbulblegacy.messaging.MessageManager;
import me.simondumalski.lightbulblegacy.messaging.enums.Log;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private Economy economy;
    private PlayerParticlesAPI ppAPI;

    private CompensationDataConfig compensationDataConfig;
    private CombatDataConfig combatDataConfig;

    private CompensationManager compensationManager;
    private CombatManager combatManager;


    @Override
    public void onEnable() {

        //Initialize the config.yml
        saveDefaultConfig();
        reloadConfig();

        //Initialize the MessageManager and ConsoleLogger
        MessageManager.setPlugin(this);
        ConsoleLogger.setLogger(getLogger());

        //Initialize the data config files
        compensationDataConfig = new CompensationDataConfig(this);
        combatDataConfig = new CombatDataConfig(this);

        //Initialize the plugin managers
        compensationManager = new CompensationManager(this);
        combatManager = new CombatManager(this);

        //Initialize the economy provider
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            ConsoleLogger.logToConsole(Log.MISSING_DEPENDENCY_SEVERE, new String[]{"Vault"});
            getServer().getPluginManager().disablePlugin(this);
        } else {
            economy = Objects.requireNonNull(getServer().getServicesManager().getRegistration(Economy.class)).getProvider();
        }

        //Initialize the PlayerParticlesAPI
        if (getServer().getPluginManager().getPlugin("PlayerParticles") == null) {
            ConsoleLogger.logToConsole(Log.MISSING_DEPENDENCY, new String[]{"PlayerParticles"});
        } else {
            ppAPI = PlayerParticlesAPI.getInstance();
        }

        //Register the event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PotionSplashListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);

        //Set the command executors
        getCommand("compensate").setExecutor(new CompensateCommand(this));
        getCommand("pvp").setExecutor(new PvpCommand(this));
        getCommand("lightbulblegacy").setExecutor(new LightbulbLegacyCommand(this));

        //Schedule the repeating tasks
        scheduleTasks();

    }

    @Override
    public void onDisable() {

        //Cancel the scheduled tasks
        cancelTasks();

        //Save the data to file
        compensationDataConfig.saveData();
        combatDataConfig.saveData();

    }

    /**
     * Reloads parts of the plugin that can be reloaded
     */
    public void reload() {
        reloadConfig();
        cancelTasks();
        scheduleTasks();
    }

    /**
     * Schedules the repeating tasks
     */
    public void scheduleTasks() {

        //Schedule the PvpCircleTask
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new PvpCircleTask(this), 100L, 10L);

        //Get the interval for the data save task
        int interval = getConfig().getInt("plugin.save-interval") * 20;

        //Check if the interval is valid
        if (interval == 0) {
            interval = 300 * 20;
            ConsoleLogger.logToConsole(Log.INVALID_INTERVAL, null);
        }

        //Schedule the TagTimerTask
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TagTimerTask(this), 20L, 20L);

        //Schedule the DataSaveTask
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new DataSaveTask(this), interval, interval);
    }

    /**
     * Cancels the repeating tasks
     */
    public void cancelTasks() {
        getServer().getScheduler().cancelTasks(this);
    }

    /**
     * Returns the instance of the plugin's Economy provider
     * @return Economy
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Returns the instance of the PlayerParticlesAPI
     * @return PlayerParticlesAPI
     */
    public PlayerParticlesAPI getPpAPI() {
        return ppAPI;
    }

    /**
     * Returns the instance of the CompensationDataConfig
     * @return CompensationDataConfig
     */
    public CompensationDataConfig getCompensationDataConfig() {
        return compensationDataConfig;
    }

    /**
     * Returns the instance of the CombatDataConfig
     * @return CombatDataConfig
     */
    public CombatDataConfig getCombatDataConfig() {
        return combatDataConfig;
    }

    /**
     * Returns the instance of the plugin's CompensationManager
     * @return CompensationManager
     */
    public CompensationManager getCompensationManager() {
        return compensationManager;
    }

    /**
     * Returns the instance of the plugin's CombatManager
     * @return CombatManager
     */
    public CombatManager getCombatManager() {
        return combatManager;
    }

}
