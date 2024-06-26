package net.jeqo.blockmaster.blocks.directional;

import net.jeqo.blockmaster.blocks.custom.CustomBlockData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.NoteBlock;

/**
 * A directional block to take into account block rotation
 */
public interface DirectionalBlock {
    /**
     *                  Places the block with the specified facing direction
     * @param block     The block to place, type org.bukkit.block.Block
     * @param facing    The facing direction of the block, type org.bukkit.block.BlockFace
     */
    default void place(Block block, BlockFace facing) {
        block.setType(Material.NOTE_BLOCK, false);
        NoteBlock data = (NoteBlock) block.getBlockData();
        block.setBlockData(getFacingData(data, facing));
    }

    /**
     *                 Gets the data of the block with the specified facing direction
     * @param data     The data of the block, type org.bukkit.block.data.type.NoteBlock
     * @param face     The facing direction of the block, type org.bukkit.block.BlockFace
     * @return         The data of the block with the specified facing direction, type org.bukkit.block.data.type.NoteBlock
     */
    NoteBlock getFacingData(NoteBlock data, BlockFace face);

    /**
     *                 Checks if the block is a variant of the custom block
     * @param data     The data of the block, type net.jeqo.blockmaster.blocks.custom.CustomBlockData
     * @return         If the block is a variant of the custom block, type boolean
     */
    boolean isVariant(CustomBlockData data);

    /**
     *                Gets the facing direction of the block
     * @param block   The block to get the facing direction from, type org.bukkit.block.Block
     * @return        The facing direction of the block, type org.bukkit.block.BlockFace
     */
    BlockFace getFacingDirection(Block block);
}
