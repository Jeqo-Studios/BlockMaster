package net.jeqo.blockmaster.configuration;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.BlockRegistry;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.logger.Logger;
import net.jeqo.blockmaster.message.Languages;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Setter @Getter
public class ConfigConfiguration {
    /**
     * The folder that stores the languages to be loaded
     */
    public static final String LANGUAGES_CONFIGURATION_FOLDER = "languages";
    /**
     * The folder that stores the block configurations
     */
    public static final String BLOCKS_CONFIGURATION_FOLDER = "blocks";

    /**
     * Copies the example blocks folder to the plugin's data folder if it doesn't exist
     */
    public static void copyExampleBlocks() {
        // List of example balloon files
        String[] exampleBlocks = new String[] {
                "/example_block.yml"
        };

        // Save all example files in the example blocks folder in /resources
        for (String example : exampleBlocks) {
            File file = new File(BlockMaster.getInstance().getDataFolder() + File.separator + ConfigConfiguration.BLOCKS_CONFIGURATION_FOLDER + File.separator + example);
            if (file.exists()) continue;

            BlockMaster.getInstance().saveResource(ConfigConfiguration.BLOCKS_CONFIGURATION_FOLDER + example, false);
        }
    }

    /**
     * Loads all blocks from the configuration files and adds them to the block registry
     */
    public static void loadBlocks() {
        File folder = new File(BlockMaster.getInstance().getDataFolder() + File.separator + BLOCKS_CONFIGURATION_FOLDER);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            Logger.logWarning(String.format(Languages.getMessage("configuration-folder-not-found"), folder.getPath()));
            return;
        }

        // List files in the folder
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            Logger.logWarning(String.format(Languages.getMessage("no-configuration-files-found"), folder.getPath()));
            return;
        }

        BlockRegistry.clear();

        // Loop through each file in the blocks directory
        for (File file : files) {
            if (file.isFile()) {
                // Load the configuration file
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                // Get the configuration section
                ConfigurationSection section = config.getConfigurationSection("");
                if (section == null) {
                    Logger.logWarning(String.format(Languages.getMessage("configuration-section-not-found"), folder.getPath()));
                    continue;
                }

                // Process each key in the section
                for (String key : section.getKeys(false)) {
                    BlockRegistry.deserialize(config, key);
                }
            }
        }

        // Log that the blocks have been loaded
        Logger.logInfo(Languages.getMessage("prefix") + "Loaded " + BlockRegistry.getRegistry().size() + " custom blocks!");
    }
}
