package net.jeqo.blockmaster.logger;

import net.jeqo.blockmaster.configuration.PluginConfiguration;
import net.jeqo.blockmaster.message.Languages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A utility class intended to log messages easily to the Bukkit console
 */
public class Logger {
    /**
     *                  Log a message to the console with STDOUT
     * @param message   The message to log, type java.lang.String
     */
    public static void logWithSTDOUT(String message) {
        System.out.println(message);
    }

    /**
     *                  Logs a message to the specified player
     * @param level     The logging level, type net.jeqo.bloons.logger.LoggingLevel
     * @param player    The player to log the message to, type org.bukkit.entity.Player
     * @param message   The message to log, type java.lang.String
     */
    public static void logToPlayer(LoggingLevel level, Player player, String message) {
        player.sendMessage(level.getColor() + "[" + level.getName() + "] " + message);
    }

    /**
     *                  Logs a message to the specified player with the plugin prefix
     * @param player    The player to log the message to, type org.bukkit.entity.Player
     * @param message   The message to log, type java.lang.String
     */
    public static void logToPlayer(Player player, String message) {
        player.sendMessage(Languages.getMessage("prefix") + message);
    }

    /**
     *                  Log a message to the console
     * @param level     The logging level, type net.jeqo.bloons.logger.LoggingLevel
     * @param message   The message to log, type java.lang.String
     */
    public static void log(LoggingLevel level, String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(level.getColor() + "[" + level.getName() + "] " + message);
    }

    /**
     *                  Logs a message to the console with the WARNING level
     * @param message   The message to log, type java.lang.String
     */
    public static void logWarning(String message) {
        log(LoggingLevel.WARNING, message);
    }

    /**
     *                  Logs a message to the console with the INFO level
     * @param message   The message to log, type java.lang.String
     */
    public static void logInfo(String message) {
        log(LoggingLevel.INFO, message);
    }

    /**
     *                  Logs a message to the console with the ERROR level
     * @param message   The message to log, type java.lang.String
     */
    public static void logError(String message) {
        log(LoggingLevel.ERROR, message);
    }

    /**
     *                  Logs a message to the console with the DEBUG level
     * @param message   The message to log, type java.lang.String
     */
    public static void logDebug(String message) {
        log(LoggingLevel.DEBUG, message);
    }

    /**
     * Logs an initialization message to the Bukkit console containing the plugin name
     */
    public static void logInitialStartup() {
        log(LoggingLevel.INFO, "Initializing " + PluginConfiguration.getName() + " plugin...");
    }

    /**
     * Logs a final startup message to the Bukkit console containing plugin information
     */
    public static void logFinalStartup() {
        // Log info messages with the plugin information
        log(LoggingLevel.INFO, PluginConfiguration.getName() + " plugin has initialized!");
        log(LoggingLevel.INFO, "Version: " + PluginConfiguration.getVersion());
        log(LoggingLevel.INFO, "Developers: " + PluginConfiguration.DEVELOPER_CREDITS);
        log(LoggingLevel.INFO, "Website: " + PluginConfiguration.getURL());
    }

    /**
     * Logs an update notification to the Bukkit console
     */
    public static void logUpdateNotificationConsole() {
        logInfo("A new update is available for " + PluginConfiguration.getName() + " plugin");
        logInfo("You can find the update at: https://jeqo.net/");
    }

    /**
     *                  Logs an update notification to a player
     * @param player    The player to log the update notification to, type org.bukkit.entity.Player
     */
//    public static void logUpdateNotificationPlayer(Player player) {
//        logToPlayer(player, "A new update is available for " + PluginConfiguration.getName() + " plugin");
//        logToPlayer(player, "You can find the update at: https://jeqo.net/");
//    }

    /**
     * Logs an initial shutdown message to the Bukkit console
     */
    public static void logInitialShutdown() {
        log(LoggingLevel.INFO, "Shutting down " + PluginConfiguration.getName() + " plugin...");
    }

    /**
     * Logs a final shutdown message to the Bukkit console
     */
    public static void logFinalShutdown() {
        log(LoggingLevel.INFO, PluginConfiguration.getName() + " plugin has been shutdown gracefully");
    }
}
