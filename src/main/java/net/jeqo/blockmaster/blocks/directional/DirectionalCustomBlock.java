package net.jeqo.blockmaster.blocks.directional;

import lombok.Getter;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.blocks.custom.CustomBlockData;
import org.apache.commons.lang3.Validate;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.NoteBlock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A directional custom block to take into account block rotation
 */
@Getter
public class DirectionalCustomBlock extends CustomBlock implements DirectionalBlock {
    private final HashMap<BlockFace, CustomBlockData> variants = new HashMap<>();

    /**
     *                              Creates a directional custom block with the specified data
     * @param key                   The key of the custom block, type java.lang.String
     * @param id                    The id of the custom block, type java.lang.String
     * @param customModelData       The custom model data of the custom block item, type int
     * @param instrument            The instrument of the note block, type org.bukkit.Instrument
     * @param note                  The note of the note block, type int
     * @param powered               If the note block is powered, type boolean
     * @param variants              The variants of the block, type java.util.HashMap[org.bukkit.block.BlockFace, net.jeqo.blockmaster.blocks.custom.CustomBlockData]
     * @param material              The material of the block, type org.bukkit.Material
     */
    public DirectionalCustomBlock(String key, String id, int customModelData, Instrument instrument, int note, boolean powered, HashMap<BlockFace, CustomBlockData> variants, Material material) {
        super(key, id, customModelData, instrument, note, powered, material);
        List<BlockFace> AVAILABLE_FACES = Arrays.asList(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN);
        AVAILABLE_FACES.forEach(f -> this.getVariants().put(f, getData()));
        Validate.isTrue(variants.size() >= 5, "Not enought face variants");
        variants.keySet().stream().filter(AVAILABLE_FACES::contains).forEach(f -> this.getVariants().put(f, variants.get(f)));
    }

    /**
     *                              Creates a directional custom block with the specified data
     * @param key                   The key of the custom block, type java.lang.String
     * @param id                    The id of the custom block, type java.lang.String
     * @param customModelData       The custom model data of the custom block item, type int
     * @param instrument            The instrument of the note block, type org.bukkit.Instrument
     * @param note                  The note of the note block, type int
     * @param powered               If the note block is powered, type boolean
     * @param variants              The variants of the block, type java.util.HashMap[org.bukkit.block.BlockFace, net.jeqo.blockmaster.blocks.custom.CustomBlockData]
     */
    public DirectionalCustomBlock(String key, String id, int customModelData, Instrument instrument, int note, boolean powered, HashMap<BlockFace, CustomBlockData> variants) {
        this(key, id, customModelData, instrument, note, powered, variants, null);
    }

    /**
     *                 Gets the data of the block with the specified facing direction
     * @param data     The data of the block, type org.bukkit.block.data.type.NoteBlock
     * @param face     The facing direction of the block, type org.bukkit.block.BlockFace
     * @return         The data of the block with the specified facing direction, type org.bukkit.block.data.type.NoteBlock
     */
    @Override
    public NoteBlock getFacingData(NoteBlock data, BlockFace face) {
        return this.getVariants().get(face).applyData(data);
    }

    /**
     *                  Checks if the block is a variant of the custom block
     * @param data      The data of the block, type net.jeqo.blockmaster.blocks.custom.CustomBlockData
     * @return          True if the block is, false if it isn't, type boolean
     */
    @Override
    public boolean isVariant(CustomBlockData data) {
        return this.getVariants().values().stream().anyMatch(d -> d.compareData(data));
    }

    /**
     *                Gets the facing direction of the block
     * @param block   The block to get the facing direction from, type org.bukkit.block.Block
     * @return        The facing direction of the block, type org.bukkit.block.BlockFace
     */
    @Override
    public BlockFace getFacingDirection(Block block) {
        Validate.isTrue(block.getBlockData() instanceof NoteBlock, "Block isn't a noteblock!");
        for (BlockFace face : this.getVariants().keySet())
            if (this.getVariants().get(face).compareData((NoteBlock) block.getBlockData()))
                return face;
        return null;
    }

}
