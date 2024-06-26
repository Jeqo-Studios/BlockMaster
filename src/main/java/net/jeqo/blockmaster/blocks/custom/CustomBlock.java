package net.jeqo.blockmaster.blocks.custom;

import lombok.Getter;
import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.directional.DirectionalBlock;
import net.jeqo.blockmaster.blocks.directional.DirectionalCustomBlock;
import net.jeqo.blockmaster.utils.ItemUtils;
import net.jeqo.blockmaster.utils.Utils;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CustomBlock {
    private static final List<CustomBlock> REGISTRY = new ArrayList<>();

    @Getter
    private final String key;
    @Getter
    private final String id;
    private final int itemModelData;
    @Getter
    private final CustomBlockData data;
    @Getter
    private final Material material;

    public CustomBlock(String key, @NotNull String id, int itemModelData, @NotNull Instrument instrument, int note, boolean powered, @Nullable Material material) {
        this.key = key;
        this.id = id;
        this.itemModelData = itemModelData;
        this.data = new CustomBlockData(instrument, note, powered);
        this.material = material;
    }

    public CustomBlock(String key, @NotNull String id, int itemModelData, @NotNull Instrument instrument, int note, boolean powered) {
        this(key, id, itemModelData, instrument, note, powered, null);
    }

    public void onPhysics(Block block, Block source) {

    }

    public void place(BlockPlaceEvent event) {
        place(event.getBlock());
    }

    public void place(@NotNull Block block) {
        block.setType(Material.NOTE_BLOCK, false);
        block.setBlockData(data.applyData((NoteBlock) block.getBlockData()));
    }

    public void mine(BlockBreakEvent event) {
        mine(event.getBlock());
        event.setExpToDrop(0);
    }

    public void mine(@NotNull Block block) {
        block.getWorld().dropItemNaturally(block.getLocation().add(.5, .5, .5), getItemStack());
    }

    public boolean compareData(@NotNull CustomBlockData data) {
        return (this instanceof DirectionalCustomBlock) ?
                ((DirectionalCustomBlock) this).isVariant(data) :
                this.data.compareData(data);
    }

    public ItemStack getItemStack() {
        return getItemStack(false);
    }

    public ItemStack getItemStack(boolean visibleBlockId) {
        ItemStack item = new ItemStack(material == null ? Material.getMaterial(BlockMaster.getInstance().getConfig().getString("menu-item-material")) : material);
        item = ItemUtils.setItemId(item, id);
        item = ItemUtils.setComponentName(item,
                new ComponentBuilder(new TranslatableComponent(String.format("customblocks.item.%s.name", id))).italic(false).create());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(itemModelData);
            meta.setDisplayName(key);
            if (visibleBlockId) meta.setLore(Collections.singletonList(Utils.colorize("&r&8" + id)));
        }
        item.setItemMeta(meta);
        return item;
    }

    ////

    public static CustomBlock getCustomBlockbyId(String id) {
        for (CustomBlock CB : REGISTRY)
            if (CB.getId().equalsIgnoreCase(id))
                return CB;
        return null;
    }

    public static CustomBlock getCustomBlockbyItem(@NotNull ItemStack item) {
        return getCustomBlockbyId(ItemUtils.getItemId(item));
    }

    public static CustomBlock getCustomBlockbyData(@NotNull CustomBlockData data) {
        for (CustomBlock CB : REGISTRY)
            if (CB.compareData(data)
                    || (CB instanceof DirectionalBlock &&
                    ((DirectionalBlock) CB).isVariant(data))) return CB;
        return null;
    }

    public static boolean isCustomBlock(Block b) {
        return b.getBlockData() instanceof NoteBlock &&
                !((NoteBlock) b.getBlockData()).getNote().equals(new Note(0));
    }

    public static boolean isCustomBlock(String id) {
        return getCustomBlockbyId(id) != null;
    }

    // REGISTRY

    public static List<CustomBlock> getRegistry() {
        return REGISTRY;
    }

    public static void register(@NotNull CustomBlock customBlock, boolean replace) {
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

    public static void unregister(@NotNull String id) {
        REGISTRY.remove(getCustomBlockbyId(id));
    }

    public static void clear() {
        REGISTRY.clear();
    }

    // SERIALIZERS
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("id", this.id);
        serialized.put("itemModelData", this.itemModelData);
        serialized.put("instrument", this.data.getInstrument().toString());
        serialized.put("note", this.data.getNote());
        serialized.put("powered", this.data.getPowered());

        if (this.material != null) serialized.put("cbItem", material);
        return serialized;
    }

    public static void deserialize(@NotNull FileConfiguration config, String key) {
        try {
            register(new CustomBlock(key, Objects.requireNonNull(config.getString(key + ".id")),
                            config.getInt(key + ".itemModelData"),
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
