package net.jeqo.blockmaster.commands;

import net.jeqo.blockmaster.blocks.BlockRegistry;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.commands.manager.Command;
import net.jeqo.blockmaster.commands.manager.types.CommandPermission;
import net.jeqo.blockmaster.message.Languages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The command to remove a block to the registry temporarily
 */
public class CommandRemove extends Command {

    /**
     *                 Constructor for the CommandRemove class
     * @param plugin   The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandRemove(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("add");
        this.setCommandDescription("Remove a block from the block registry temporarily until a server reload or cache update.");
        this.setCommandSyntax("/blockmaster remove <id>");
        this.setRequiredPermission(CommandPermission.REMOVE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        try {
            BlockRegistry.unregister(args[0]);

            sender.sendMessage(Languages.getMessage("prefix") + "Block " + args[0] + " removed temporarily successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Languages.getMessage("prefix") + "Block " + args[0] + " cannot be added, please check the logs");
            return false;
        }
        return false;
    }
}