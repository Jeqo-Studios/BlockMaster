package net.jeqo.blockmaster;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.blockmaster.logger.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockMaster extends JavaPlugin {
    @Getter @Setter
    private static BlockMaster instance;

    @Override
    public void onEnable() {
        // Create an instance of the plugin
        setInstance(this);

        // Log the initial startup message
        Logger.logInitialStartup();


        // Log the final startup message
        Logger.logFinalStartup();
    }

    @Override
    public void onDisable() {
        // Log the initial shutdown message
        Logger.logInitialShutdown();


        // Log the final shutdown message
        Logger.logFinalShutdown();
    }
}
