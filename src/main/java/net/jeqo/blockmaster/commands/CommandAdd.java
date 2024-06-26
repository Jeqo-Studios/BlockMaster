package net.jeqo.blockmaster.commands;

import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.commands.manager.Command;
import net.jeqo.blockmaster.commands.manager.types.CommandPermission;
import net.jeqo.blockmaster.message.Languages;
import org.bukkit.Instrument;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The command to add a block to the registry temporarily
 */
public class CommandAdd extends Command {

    /**
     *                 Constructor for the CommandAdd class
     * @param plugin   The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandAdd(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("add");
        this.setCommandDescription("Adds a block to the registry temporarily until a server reload.");
        this.setCommandSyntax("/blockmaster add <id> <customModelData> <instrument> <note> <powered>");
        this.setRequiredPermission(CommandPermission.ADD);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 5) return false;
        try {
            // Create and register a custom block with the given arguments temporarily
            CustomBlock customBlock = new CustomBlock(args[0], args[0], Integer.parseInt(args[1]), Instrument.valueOf(args[2]), Integer.parseInt(args[3]), args[4].equalsIgnoreCase("true"));
            CustomBlock.register(customBlock, false);

            sender.sendMessage(Languages.getMessage("prefix") + "Block " + args[0] + " added sucessfully");
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Languages.getMessage("prefix") + "Block " + args[0] + " cannot be added, please check the logs");
            return false;
        }
        return false;
    }
}
