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

public class ItemUtils {
    public static boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    public static ItemStack getBlockOrCustomBlockInHand(@NotNull PlayerInventory inv) {
        return (inv.getItemInMainHand().getType().isBlock() || CustomBlock.getCustomBlockByItem(inv.getItemInMainHand()) != null) && inv.getItemInMainHand().getType() != Material.AIR
                ? inv.getItemInMainHand()
                : (inv.getItemInOffHand().getType().isBlock() || CustomBlock.getCustomBlockByItem(inv.getItemInOffHand()) != null) && inv.getItemInOffHand().getType() != Material.AIR
                ? inv.getItemInOffHand()
                : null;
    }

    public static EquipmentSlot getEquipmentSlot(PlayerInventory inv, ItemStack item) {
        return inv.getItemInMainHand().equals(item) ? EquipmentSlot.HAND
                : inv.getItemInOffHand().equals(item) ? EquipmentSlot.OFF_HAND : null;
    }

    public static String getItemId(@NotNull ItemStack item) {
        return new NBTItem(item).getStringFlag("ItemId");
    }

    public static ItemStack setItemId(@NotNull ItemStack item, @NotNull String id) {
        NBTItem nbti = new NBTItem(item);
        nbti.setStringFlag("ItemId", id);
        return nbti.getItem();
    }

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

    public static ItemStack setComponentName(@NotNull ItemStack item, @NotNull BaseComponent[] component) {
        NBTItem nbti = new NBTItem(item);
        nbti.setStringFlag("Name", ComponentSerializer.toString(component));
        return nbti.getItem();
    }
}
