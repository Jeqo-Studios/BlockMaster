package net.jeqo.blockmaster.gui;

import net.jeqo.blockmaster.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.UUID;

/**
 * Contains the methods to fetch the items that are supposed to be used in the BlockMaster GUI
 */
public class BlockMasterGUIItems {

    /**
     *          Gets the back button item stack
     * @return  The back button item stack, type org.bukkit.inventory.ItemStack
     */
    public static ItemStack getBackButton() {
        String texture = "8652e2b936ca8026bd28651d7c9f2819d2e923697734d18dfdb13550f8fdad5f";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTexture(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Essentials.colorize("&aBack"));
        item.setItemMeta(meta);
        return item;
    }

    /**
     *                      Gets the info button item stack
     * @param page          The current page, type int
     * @param totalPages    The total amount of pages, type int
     * @return              The info button item stack with the proper name, type org.bukkit.inventory.ItemStack
     */
    public static ItemStack getInfoButton(int page, int totalPages) {
        String texture = "2e3f50ba62cbda3ecf5479b62fedebd61d76589771cc19286bf2745cd71e47c6";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTexture(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Essentials.colorize("&aPage " + page + "/" + totalPages));
        meta.setLore(Collections.singletonList(Essentials.colorize("&a&lLeft Click &r&8Go back to page 1")));
        item.setItemMeta(meta);
        return item;
    }

    /**
     *          Gets the next button item stack
     * @return  The next button item stack, type org.bukkit.inventory.ItemStack
     */
    public static ItemStack getNextButton() {
        String texture = "2a3b8f681daad8bf436cae8da3fe8131f62a162ab81af639c3e0644aa6abac2f";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTexture(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Essentials.colorize("&aNext"));
        item.setItemMeta(meta);
        return item;
    }

    /**
     *                  Gets a skull texture based off of a Minecraft texture ID
     * @param meta      The skull meta to set the texture to, type org.bukkit.inventory.meta.SkullMeta
     * @param texture   The texture ID to set the skull to, type java.lang.String
     * @return          The skull meta with the texture set, type org.bukkit.inventory.meta.SkullMeta
     */
    public static SkullMeta getSkullMetaTexture(SkullMeta meta, String texture) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"));
        PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(URI.create("https://textures.minecraft.net/texture/" + texture).toURL());
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }

        profile.setTextures(textures);

        meta.setOwnerProfile(profile);

        return meta;
    }
}
