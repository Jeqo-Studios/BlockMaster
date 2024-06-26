package net.jeqo.blockmaster.utils;

import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Contains item related utilities to aid and assist in item manipulation
 */
public class ItemUtils {
    /**
     *                 Check if an item is air or null
     * @param item     The item to check, type org.bukkit.inventory.ItemStack
     * @return         Whether the item is air or null, type boolean
     */
    public static boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    /**
     *                  Get the block or custom block in the player's hand
     * @param inv       The player's inventory, type org.bukkit.inventory.PlayerInventory
     * @return          The block or custom block in the player's hand, type org.bukkit.inventory.ItemStack
     */
    public static ItemStack getBlockOrCustomBlockInHand(@NotNull PlayerInventory inv) {
        return (inv.getItemInMainHand().getType().isBlock() || CustomBlock.getCustomBlockByItem(inv.getItemInMainHand()) != null) && inv.getItemInMainHand().getType() != Material.AIR
                ? inv.getItemInMainHand()
                : (inv.getItemInOffHand().getType().isBlock() || CustomBlock.getCustomBlockByItem(inv.getItemInOffHand()) != null) && inv.getItemInOffHand().getType() != Material.AIR
                ? inv.getItemInOffHand()
                : null;
    }

    /**
     *                 Get the equipment slot of the item in the player's hand
     * @param inv      The player's inventory, type org.bukkit.inventory.PlayerInventory
     * @param item     The item to check, type org.bukkit.inventory.ItemStack
     * @return         The equipment slot of the item in the player's hand, type org.bukkit.inventory.EquipmentSlot
     */
    public static EquipmentSlot getEquipmentSlot(PlayerInventory inv, ItemStack item) {
        return inv.getItemInMainHand().equals(item) ? EquipmentSlot.HAND
                : inv.getItemInOffHand().equals(item) ? EquipmentSlot.OFF_HAND : null;
    }

    /**
     *                 Get the item ID of the item
     * @param item     The item to get the ID of, type org.bukkit.inventory.ItemStack
     * @return         The item ID of the item, type java.lang.String
     */
    public static String getItemId(@NotNull ItemStack item) {
        return new NBTItem(item).getStringFlag("ItemId");
    }

    /**
     *                Set the item ID of the item
     * @param item    The item to set the ID of, type org.bukkit.inventory.ItemStack
     * @param id      The ID to set, type java.lang.String
     * @return        The item with the ID set, type org.bukkit.inventory.ItemStack
     */
    public static ItemStack setItemId(@NotNull ItemStack item, @NotNull String id) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setStringFlag("ItemId", id);
        return nbtItem.getItem();
    }

    /**
     *                  Get the first custom block in the player's hands
     * @param inv       The player's inventory, type org.bukkit.inventory.PlayerInventory
     * @return          The first custom block in the player's hands, type org.bukkit.inventory.ItemStack
     */
    public static ItemStack getFirstCustomBlockInHand(@NotNull PlayerInventory inv) {
        ItemStack[] hands = new ItemStack[]{inv.getItemInMainHand(), inv.getItemInOffHand()};

        for (ItemStack i : hands) {
            if (ItemUtils.isAirOrNull(i)) continue;
            CustomBlock CB = CustomBlock.getCustomBlockByItem(i);
            if (CB == null) continue;
            Material expected = CB.getMaterial() != null ? CB.getMaterial() : Material.matchMaterial(BlockMaster.getInstance().getConfig().getString("menu-item-material"));
            if (i.getType() == expected) return i;
        }
        return null;
    }

    /**
     *                      Sets the name NBT data for an item
     * @param item          The item to set the name of, type org.bukkit.inventory.ItemStack
     * @param component     The component to set the name to, type net.md_5.bungee.api.chat.BaseComponent[]
     * @return              The item with the name set, type org.bukkit.inventory.ItemStack
     */
    public static ItemStack setComponentName(@NotNull ItemStack item, @NotNull BaseComponent[] component) {
        NBTItem nbti = new NBTItem(item);
        nbti.setStringFlag("Name", ComponentSerializer.toString(component));
        return nbti.getItem();
    }
}
