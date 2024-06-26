package net.jeqo.blockmaster;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.blockmaster.blocks.api.BlockMasterLoadEvent;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.commands.manager.CommandCore;
import net.jeqo.blockmaster.configuration.ConfigConfiguration;
import net.jeqo.blockmaster.listeners.BlockListeners;
import net.jeqo.blockmaster.listeners.InventoryListeners;
import net.jeqo.blockmaster.listeners.ListenerCore;
import net.jeqo.blockmaster.logger.Logger;
import net.jeqo.blockmaster.message.Languages;
import net.jeqo.blockmaster.nms.CommonNMSHandler;
import net.jeqo.blockmaster.nms.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockMaster extends JavaPlugin {
    @Getter @Setter
    private static BlockMaster instance;
    @Getter @Setter
    private static NMSHandler nmsHandler;
    @Getter @Setter
    private static CommandCore commandCore;
    @Getter @Setter
    private static ListenerCore listenerCore;

    @Override
    public void onEnable() {
        // Create an instance of the plugin
        setInstance(this);

        // Log the initial startup message
        Logger.logInitialStartup();

        // Clear the custom blocks list
        CustomBlock.clear();

        // Copy all language files over from the languages directory
        Languages.copyLanguageFiles();

        // Generate config(s) and set defaults
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        ConfigConfiguration.copyExampleBlocks();

        // Set instances of managers
        setCommandCore(new CommandCore(this));
        setListenerCore(new ListenerCore(this));

        // Set the NMS handler
        final String mcVersion = CommonUtils.getMinecraftVersion(),
                nmsVersion = CommonUtils.getNMSVersion();
        Logger.logInfo(Languages.getMessage("prefix") + "Loading NMS Handler...");
        nmsHandler = NMSHandler.getHandler(mcVersion, nmsVersion);
        if (nmsHandler == null) {
            Logger.logInfo(Languages.getMessage("prefix") + CommonUtils.colorize("&cNo NMSHandler found! Using common one"));
            try {
                nmsHandler = new CommonNMSHandler();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.logInfo(Languages.getMessage("prefix") + CommonUtils.colorize("&cCommonNMS can't support this version! Disabling plugin"));
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

        // Load the blocks from the config
        ConfigConfiguration.loadBlocks();

        // Stage all listeners for registration
        getListenerCore().stageListener(new BlockListeners());
        getListenerCore().stageListener(new InventoryListeners());

        // Register all handlers
        getListenerCore().registerListeners();

        Bukkit.getPluginManager().callEvent(new BlockMasterLoadEvent(false));

        // Log the final startup message
        Logger.logFinalStartup();
    }

        @Override
        public void onDisable () {
            // Log the initial shutdown message
            Logger.logInitialShutdown();

            // Unregister all listeners in the manager
            getListenerCore().unregisterListeners();

            // Log the final shutdown message
            Logger.logFinalShutdown();
        }
}
