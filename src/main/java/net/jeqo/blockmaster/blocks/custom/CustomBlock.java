package net.jeqo.blockmaster.blocks.custom;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.BlockRegistry;
import net.jeqo.blockmaster.blocks.directional.DirectionalCustomBlock;
import net.jeqo.blockmaster.items.ItemUtils;
import net.jeqo.blockmaster.Essentials;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The class that represents a custom block within BlockMaster
 */
@Getter @Setter
public class CustomBlock {
    /**
     * The configuration key of the blocks configuration
     */
    private String key;
    /**
     * The ID of the block
     */
    private String id;
    /**
     * The custom model data of the block item given to players
     */
    private int customModelData;
    /**
     * The custom data of the block
     */
    private CustomBlockData data;
    /**
     * The material of the block item
     */
    private Material material;

    /**
     *                          The constructor used to create a custom block
     * @param key               The configuration key for the block configuration, type java.lang.String
     * @param id                The ID of the block, type java.lang.String
     * @param customModelData   The custom model data of the block item, type int
     * @param instrument        The instrument of the block, type org.bukkit.Instrument
     * @param note              The note value that the noteblock is set to, type int
     * @param powered           The powered state of the noteblock, type boolean
     * @param material          The material of the block item, type org.bukkit.Material
     */
    public CustomBlock(String key, String id, int customModelData, Instrument instrument, int note, boolean powered, Material material) {
        this.setKey(key);
        this.setId(id);
        this.setCustomModelData(customModelData);
        this.setData(new CustomBlockData(instrument, note, powered));
        this.setMaterial(material);
    }

    /**
     *                          The constructor used to create a custom block with a material of null
     * @param key               The configuration key for the block configuration, type java.lang.String
     * @param id                The ID of the block, type java.lang.String
     * @param customModelData   The custom model data of the block item, type int
     * @param instrument        The instrument of the block, type org.bukkit.Instrument
     * @param note              The note value that the noteblock is set to, type int
     * @param powered           The powered state of the noteblock, type boolean
     */
    public CustomBlock(String key, String id, int customModelData, Instrument instrument, int note, boolean powered) {
        this(key, id, customModelData, instrument, note, powered, null);
    }

    /**
     *              Triggered on a block break event and runs the logic for block placements
     * @param event The event of the block being placed, type org.bukkit.event.block.BlockPlaceEvent
     */
    public void place(BlockPlaceEvent event) {
        place(event.getBlock());
    }

    /**
     *              Places the block with the custom data and block properties
     * @param block The block to place, type org.bukkit.block.Block
     */
    public void place(Block block) {
        block.setType(Material.NOTE_BLOCK, false);
        block.setBlockData(this.getData().applyData((NoteBlock) block.getBlockData()));
    }

    /**
     *              Mines the block and drops the item and 0 XP
     * @param event The event of the block being mined, type org.bukkit.event.block.BlockBreakEvent
     */
    public void mine(BlockBreakEvent event) {
        rewardCustomBlockItem(event.getBlock());
        event.setExpToDrop(0);
    }

    /**
     *              Rewards the player with the custom block item when the block is mined
     * @param block The block to reward the player with, type org.bukkit.block.Block
     */
    public void rewardCustomBlockItem(Block block) {
        block.getWorld().dropItemNaturally(block.getLocation().add(.5, .5, .5), getItemStack());
    }

    /**
     *              Compares the data of the custom block with the provided data
     * @param data  The data to compare, type net.jeqo.blockmaster.blocks.custom.CustomBlockData
     * @return      True if the data matches, false otherwise
     */
    public boolean compareData(CustomBlockData data) {
        return (this instanceof DirectionalCustomBlock) ?
                ((DirectionalCustomBlock) this).isVariant(data) :
                this.getData().compareData(data);
    }

    /**
     *          Gets the item given to players when they want to place a block with the block ID as false
     * @return  The item, type org.bukkit.inventory.ItemStack
     */
    public ItemStack getItemStack() {
        return getItemStack(false);
    }

    /**
     *                          Gets the item given to players when they want to place a block
     * @param visibleBlockId    If the block ID should be visible
     * @return                  The item, type org.bukkit.inventory.ItemStack
     */
    public ItemStack getItemStack(boolean visibleBlockId) {
        ItemStack item = new ItemStack(this.getMaterial() == null ? Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(BlockMaster.getInstance().getConfig().getString("menu-item-material")))) : this.getMaterial());
        item = ItemUtils.setItemId(item, this.getId());
        item = ItemUtils.setComponentName(item,
                new ComponentBuilder(new TranslatableComponent(String.format("customblocks.item.%s.name", this.getId()))).italic(false).create());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(this.getCustomModelData());
            meta.setDisplayName(this.getKey());
            if (visibleBlockId) meta.setLore(Collections.singletonList(Essentials.colorize("&r&8" + this.getId())));
        }
        item.setItemMeta(meta);

        return item;
    }

    /**
     *                  Checks if a block is a custom block
     * @param block     The block to check, type org.bukkit.block.Block
     * @return          True if the block is a custom block, false otherwise
     */
    public static boolean isCustomBlock(Block block) {
        return block.getBlockData() instanceof NoteBlock &&
                !((NoteBlock) block.getBlockData()).getNote().equals(new Note(0));
    }

    /**
     *              Checks if there is a custom block in the registry with the specified ID
     * @param id    The ID of the custom block to check for
     * @return      True if there is a custom block with the specified ID, false otherwise
     */
    public static boolean isCustomBlock(String id) {
        return BlockRegistry.getCustomBlockbyId(id) != null;
    }
}
