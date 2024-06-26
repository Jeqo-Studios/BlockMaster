package net.jeqo.blockmaster.blocks.api;

import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.blocks.custom.CustomBlockData;
import net.jeqo.blockmaster.configuration.ConfigConfiguration;
import net.jeqo.blockmaster.gui.BlockMasterGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BlockMasterAPI {
    /**
     *          Gets the custom block registry list
     * @return  The list of custom blocks, type java.util.List[net.jeqo.blockmaster.blocks.custom.CustomBlock]
     */
    public static List<CustomBlock> getRegistry() {
        return CustomBlock.getRegistry();
    }

    /**
     * Clears the custom block registry
     */
    public static void clearRegistry() {
        CustomBlock.clear();
    }

    /**
     *                 Registers a set of provided custom blocks
     * @param blocks   The custom blocks to register, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     */
    public static void register(@NotNull CustomBlock... blocks) {
        register(false, blocks);
    }

    public static void register(boolean replaceIfExists, @NotNull CustomBlock... blocks) {
        Arrays.stream(blocks).forEach(block -> CustomBlock.register(block, replaceIfExists));
    }

    /**
     *                  Unregisters a set of provided custom blocks
     * @param blocks    The custom blocks to unregister, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     */
    public static void unregister(@NotNull CustomBlock... blocks) {
        Arrays.stream(blocks).map(CustomBlock::getId).forEach(CustomBlock::unregister);
    }

    /**
     *                 Gets a custom block by its id
     * @param id       The id of the custom block, type java.lang.String
     * @return         The custom block, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     */
    public static CustomBlock getCustomBlock(@NotNull String id) {
        return CustomBlock.getCustomBlockbyId(id);
    }

    /**
     *                 Gets a custom block by its item
     * @param item     The item of the custom block, type org.bukkit.inventory.ItemStack
     * @return         The custom block, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     */
    public static CustomBlock getCustomBlock(@NotNull ItemStack item) {
        return CustomBlock.getCustomBlockByItem(item);
    }

    /**
     *              Gets a custom block that matches the provided data
     * @param data  The data to match, type net.jeqo.blockmaster.blocks.custom.CustomBlockData
     * @return      The custom block, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     */
    public static CustomBlock getCustomBlock(@NotNull CustomBlockData data) {
        return CustomBlock.getCustomBlockbyData(data);
    }

    /**
     *                  Opens the block master GUI for the specified player
     * @param player    The player to open the GUI for, type org.bukkit.entity.Player
     */
    public static void openGUI(@NotNull Player player) {
        BlockMasterGUI.open(player);
    }

    /**
     *                 Opens the block master GUI for the specified player on the specified page
     * @param player   The player to open the GUI for, type org.bukkit.entity.Player
     * @param page      The page to open the GUI on, type int
     */
    public static void openGUI(@NotNull Player player, int page) {
        BlockMasterGUI.open(player, page);
    }

    /**
     * Reloads the BlockMaster block and main configurations
     */
    public static void reload() {
        ConfigConfiguration.loadBlocks();
        BlockMaster.getInstance().reloadConfig();
    }
}
