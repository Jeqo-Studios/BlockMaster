package net.jeqo.blockmaster.nms;

import net.jeqo.blockmaster.logger.Logger;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class NMSHandler {
    public static NMSHandler getHandler(@NotNull String mcVersion, @NotNull String nmsVersion) {
        Logger.logInfo(String.format("Detected version: %s (%s)", nmsVersion, mcVersion));
        String nmsPackage = NMSHandler.class.getPackage().getName();
        Class<?> clazz;
        try {
            clazz = Class.forName(String.format("%s.NMS_%s", nmsPackage, nmsVersion));
            if (!NMSHandler.class.isAssignableFrom(clazz)) return null;
            return (NMSHandler) clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    abstract public void swingHand(@NotNull Player player, EquipmentSlot slot);

    abstract public void placeItem(@NotNull Player player, @NotNull ItemStack item, @NotNull Block block, EquipmentSlot slot);

    abstract public Object _genMOPB(@NotNull Player player, @NotNull Block block);
}
