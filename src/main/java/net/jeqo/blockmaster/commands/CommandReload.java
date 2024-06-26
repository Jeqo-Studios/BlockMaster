package net.jeqo.blockmaster.commands;

import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.commands.manager.Command;
import net.jeqo.blockmaster.commands.manager.types.CommandPermission;
import net.jeqo.blockmaster.configuration.ConfigConfiguration;
import net.jeqo.blockmaster.message.Languages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A command used to reload the configurations
 */
public class CommandReload extends Command {

    /**
     *                 Constructor for the CommandReload class
     * @param plugin   The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandReload(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("reload");
        this.addCommandAlias("rl");
        this.setCommandDescription("Reload the configuration files");
        this.setCommandSyntax("/blockmaster reload");
        this.setRequiredPermission(CommandPermission.RELOAD);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Reload the main config.yml and its defaults
        BlockMaster.getInstance().reloadConfig();
        BlockMaster.getInstance().getConfig().options().copyDefaults();
        BlockMaster.getInstance().saveDefaultConfig();

        // Refresh blocks and their configurations from their respective files
        ConfigConfiguration.loadBlocks();

        // Send a message to the sender that the configuration files have been reloaded
        sender.sendMessage(Languages.getMessage("prefix"), Languages.getMessage("config-reloaded"));

        return false;
    }
}
