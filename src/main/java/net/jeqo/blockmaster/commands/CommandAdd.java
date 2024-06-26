package net.jeqo.blockmaster.commands;

import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.commands.manager.Command;
import net.jeqo.blockmaster.commands.manager.types.CommandPermission;
import net.jeqo.blockmaster.configuration.ConfigConfiguration;
import net.jeqo.blockmaster.message.Languages;
import org.bukkit.Instrument;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAdd extends Command {

    /**
     *                 Constructor for the CommandADd class
     * @param plugin   The instance of the plugin, type org.bukkit.plugin.java.JavaPlugin
     */
    public CommandAdd(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("add");
        this.setCommandDescription("Adds a block to the registry.");
        this.setCommandSyntax("/blockmaster add <id> <itemModelData> <instrument> <note> <powered>");
        this.setRequiredPermission(CommandPermission.RELOAD);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 5) return false;
        try {
            CustomBlock cb = new CustomBlock(args[1], Integer.parseInt(args[2]), Instrument.valueOf(args[3]), Integer.parseInt(args[4]), args[5].equalsIgnoreCase("true"));
            CustomBlock.register(cb, false);

            //config.blocks.add(cb.serialize());

            sender.sendMessage(Languages.getMessage("prefix") + "Block " + args[1] + " added sucessfully");
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Languages.getMessage("prefix") + "Block " + args[1] + " cannot be added, please check the logs");
            return false;
        }
        // CustomBlocksPlugin.configData.saveBlocks();
        return false;
    }
}
