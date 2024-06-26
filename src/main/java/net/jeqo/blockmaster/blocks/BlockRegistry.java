package net.jeqo.blockmaster.blocks;

import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.blocks.custom.CustomBlockData;
import net.jeqo.blockmaster.blocks.directional.DirectionalBlock;
import net.jeqo.blockmaster.items.ItemUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockRegistry {
    private static final List<CustomBlock> REGISTRY = new ArrayList<>();

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
                            config.getInt(key + ".item.custom-model-data"),
                            Instrument.valueOf(config.getString(key + ".block.instrument")),
                            config.getInt(key + ".block.note"),
                            config.getBoolean(key + ".block.powered"),
                            config.getString(key + ".item") != null ? Material.valueOf(config.getString("item")) : null),
                    false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
