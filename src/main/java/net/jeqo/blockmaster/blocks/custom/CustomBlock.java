package net.jeqo.blockmaster.blocks.custom;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.directional.DirectionalBlock;
import net.jeqo.blockmaster.blocks.directional.DirectionalCustomBlock;
import net.jeqo.blockmaster.items.ItemUtils;
import net.jeqo.blockmaster.CommonUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.apache.commons.lang3.Validate;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.configuration.file.FileConfiguration;
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
    private static final List<CustomBlock> REGISTRY = new ArrayList<>();

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
     *                  In the case of a physics event, this method is called
     * @param block     The block that the physics event is triggered on
     * @param source    The source of the physics event
     */
    public void onPhysics(Block block, Block source) { }

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
        mine(event.getBlock());
        event.setExpToDrop(0);
    }

    /**
     *                  Drops the item when the block is mined instead of the block itself
     * @param block     The block to mine, type org.bukkit.block.Block
     */
    public void mine(Block block) {
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
        ItemStack item = new ItemStack(this.getMaterial() == null ? Material.getMaterial(BlockMaster.getInstance().getConfig().getString("menu-item-material")) : this.getMaterial());
        item = ItemUtils.setItemId(item, this.getId());
        item = ItemUtils.setComponentName(item,
                new ComponentBuilder(new TranslatableComponent(String.format("customblocks.item.%s.name", this.getId()))).italic(false).create());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(this.getCustomModelData());
            meta.setDisplayName(this.getKey());
            if (visibleBlockId) meta.setLore(Collections.singletonList(CommonUtils.colorize("&r&8" + this.getId())));
        }
        item.setItemMeta(meta);

        return item;
    }

    /**
     *              Gets a custom block by its ID
     * @param id    The id of the custom block, type java.lang.String
     * @return      The custom block, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     */
    public static CustomBlock getCustomBlockbyId(String id) {
        for (CustomBlock customBlock : REGISTRY)
            if (customBlock.getId().equalsIgnoreCase(id))
                return customBlock;
        return null;
    }

    /**
     *              Gets a custom block by its item
     * @param item  The item of the custom block, type org.bukkit.inventory.ItemStack
     * @return      The custom block, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     */
    public static CustomBlock getCustomBlockByItem(ItemStack item) {
        return getCustomBlockbyId(ItemUtils.getItemId(item));
    }

    /**
     *              Gets a custom block that matches the provided data
     * @param data  The data to match, type net.jeqo.blockmaster.blocks.custom.CustomBlockData
     * @return      The custom block, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     */
    public static CustomBlock getCustomBlockbyData(CustomBlockData data) {
        for (CustomBlock CB : REGISTRY)
            if (CB.compareData(data)
                    || (CB instanceof DirectionalBlock &&
                    ((DirectionalBlock) CB).isVariant(data))) return CB;
        return null;
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
        return getCustomBlockbyId(id) != null;
    }

    /**
     *          Gets the block registry
     * @return  The block registry, type java.util.List[net.jeqo.blockmaster.blocks.custom.CustomBlock]
     */
    public static List<CustomBlock> getRegistry() {
        return REGISTRY;
    }

    /**
     *                      Registers a custom block with a boolean of if it should replace an existing configuration
     * @param customBlock   The custom block to register, type net.jeqo.blockmaster.blocks.custom.CustomBlock
     * @param replace       The boolean of if it should replace an existing configuration, type java.lang.Boolean
     */
    public static void register(CustomBlock customBlock, boolean replace) {
        String id = customBlock.getId();
        if (replace && getCustomBlockbyId(id) != null) unregister(customBlock.getId());
        else {
            Validate.isTrue(getCustomBlockbyId(id) == null,
                    String.format("Registration failed! Duplicated CustomBlock id: \"%s\"", id));
            CustomBlock clonedCB = getCustomBlockbyData(customBlock.getData());
            Validate.isTrue(clonedCB == null,
                    String.format("Registration failed! Duplicated CustomBlock data: \"%s\" <-> \"%s\"", id, clonedCB != null ? clonedCB.getId() : ""));
        }
        REGISTRY.add(customBlock);

    }

    /**
     *             Unregisters a custom block by its ID
     * @param id   The id of the custom block, type java.lang.String
     */
    public static void unregister(String id) {
        REGISTRY.remove(getCustomBlockbyId(id));
    }

    /**
     * Clears the block registry and wipes all data pertaining to blocks
     */
    public static void clear() {
        REGISTRY.clear();
    }

    /**
     *                  Creates a custom block from a configuration file and key and adds it to the registry
     * @param config    The configuration file, type org.bukkit.configuration.file.FileConfiguration
     * @param key       The key of the custom block, type java.lang.String
     */
    public static void deserialize(FileConfiguration config, String key) {
        try {
            register(new CustomBlock(key, Objects.requireNonNull(config.getString(key + ".id")),
                            config.getInt(key + ".customModelData"),
                            Instrument.valueOf(config.getString(key + ".instrument")),
                            config.getInt(key + ".note"),
                            config.getBoolean(key + ".powered"),
                            config.getString(key + ".item") != null ? Material.valueOf(config.getString("item")) : null),
                    false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
