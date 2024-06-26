package net.jeqo.blockmaster;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;

import java.util.List;
import java.util.function.Predicate;

/**
 * Common utilities used throughout the plugin
 */
public class Essentials {

    /**
     *              Gets the NMS version of the server
     * @return      The NMS version of the server with the full format, type java.lang.String
     */
    public static String getNMSVersion() {
        String version = BlockMaster.getInstance().getServer().getClass().getPackage().getName();
        return version.substring(version.lastIndexOf('.') + 1);
    }

    /**
     *              Gets just the Minecraft version of the server
     * @return      The Minecraft version of the server, type java.lang.String
     */
    public static String getMinecraftVersion() {
        String version = Bukkit.getBukkitVersion();
        return version.substring(0, version.indexOf('-'));
    }

    /**
     *                              Gets the raytraced interaction point of a player with a block
     * @param start                 The start location of the interaction, type org.bukkit.Location
     * @param maxDistance           The maximum distance the player can interact with the block, type int
     * @param ignorePassableBlocks  Whether to ignore passable blocks, type boolean
     * @return                      The location of the interaction point, type org.bukkit.Location
     */
    public static Location getInteractionPoint(Location start, int maxDistance, boolean ignorePassableBlocks) {
        if (start.getWorld() == null) return null;
        RayTraceResult rtr = start.getWorld().rayTraceBlocks(start,
                start.getDirection(), maxDistance, FluidCollisionMode.NEVER, ignorePassableBlocks);
        if (rtr == null || rtr.getHitBlock() == null) return null;
        return rtr.getHitPosition().subtract(rtr.getHitBlock().getLocation().toVector())
                .toLocation(start.getWorld());
    }

    /**
     *                          Gets the entities on a block that meet the provided predicate
     * @param block             The block to check for entities, type org.bukkit.block.Block
     * @param predicate         The predicate to check for entities, type java.util.function.Predicate
     * @return                  The list of entities on the block, type java.util.List[org.bukkit.entity.Entity]
     */
    public static List<Entity> getEntitiesOnBlock(Block block, Predicate<Entity> predicate) {
        Location loc = block.getLocation().add(.5, .5, .5);
        return (List<Entity>) block.getWorld().getNearbyEntities(loc, .5, .5, .5, predicate);
    }

    /**
     *                  Colorizes a string with Bukkit color codes
     * @param text      The text to colorize, type java.lang.String
     * @return          The colorized text, type java.lang.String
     */
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
