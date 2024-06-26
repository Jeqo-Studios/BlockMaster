package net.jeqo.blockmaster.gui;

import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

/**
 * Represents a GUI for the BlockMaster plugin to fetch and use custom blocks
 */
public class BlockMasterGUI {
    /**
     * The saved pages for each player
     */
    public static Map<UUID, Integer> savedPages = new HashMap<>();

    /**
     *                  Opens the GUI for the specified player on page 1
     * @param player    The player to open the GUI for, type org.bukkit.entity.Player
     */
    public static void open(Player player) {
        open(player, savedPages.getOrDefault(player.getUniqueId(), 1));
    }

    /**
     *                 Opens the GUI for the specified player on the specified page
     * @param player   The player to open the GUI for, type org.bukkit.entity.Player
     * @param page     The page to open the GUI on, type int
     */
    public static void open(Player player, int page) {
        int customBlocksCount = CustomBlock.getRegistry().size(), totalPages = getTotalPages();

        page = Math.max(1, Math.min(page, totalPages)); // Set page range to 1 >= page >= totalPages

        Inventory inv = Bukkit.createInventory(null, 54, BlockMaster.getInstance().getConfig().getString("menu-title") + Utils.colorize(" [" + page + "/" + totalPages + "]"));

        for (int i = (9 * 5); i < (9 * 6); i++) {
            ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.RESET + "");
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        for (int invSlot = 0, cbksIndex = (page - 1) * 45;
             invSlot < 45 && cbksIndex < customBlocksCount; invSlot++, cbksIndex++)
            inv.setItem(invSlot, CustomBlock.getRegistry().get(cbksIndex).getItemStack(true));

        inv.setItem(45, getBackButton());
        inv.setItem(49, getInfoButton(page, totalPages));
        inv.setItem(53, getNextButton());

        player.openInventory(inv);
        savedPages.put(player.getUniqueId(), page);
    }

    /**
     *          Gets the total amount of pages in the GUI based off of the blocks in the registry
     * @return  The total amount of pages in the GUI, type int
     */
    public static int getTotalPages() {
        int cbksCount = CustomBlock.getRegistry().size(),
                totalPages = cbksCount / (9 * 5);    // 45 -> The max blocks per page
        if (cbksCount % (9 * 5) > 0) ++totalPages;   // extra page for the last items
        return totalPages;
    }

    /**
     *          Gets the back button item stack
     * @return  The back button item stack, type org.bukkit.inventory.ItemStack
     */
    private static ItemStack getBackButton() {
        String texture = "8652e2b936ca8026bd28651d7c9f2819d2e923697734d18dfdb13550f8fdad5f";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTexture(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Utils.colorize("&aBack"));
        item.setItemMeta(meta);
        return item;
    }

    /**
     *                      Gets the info button item stack
     * @param page          The current page, type int
     * @param totalPages    The total amount of pages, type int
     * @return              The info button item stack with the proper name, type org.bukkit.inventory.ItemStack
     */
    private static ItemStack getInfoButton(int page, int totalPages) {
        String texture = "2e3f50ba62cbda3ecf5479b62fedebd61d76589771cc19286bf2745cd71e47c6";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTexture(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Utils.colorize("&aPage " + page + "/" + totalPages));
        meta.setLore(Collections.singletonList(Utils.colorize("&a&lLeft Click &r&8Go back to page 1")));
        item.setItemMeta(meta);
        return item;
    }

    /**
     *          Gets the next button item stack
     * @return  The next button item stack, type org.bukkit.inventory.ItemStack
     */
    private static ItemStack getNextButton() {
        String texture = "2a3b8f681daad8bf436cae8da3fe8131f62a162ab81af639c3e0644aa6abac2f";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTexture(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Utils.colorize("&aNext"));
        item.setItemMeta(meta);
        return item;
    }

    /**
     *                  Gets a skull texture based off of a Minecraft texture ID
     * @param meta      The skull meta to set the texture to, type org.bukkit.inventory.meta.SkullMeta
     * @param texture   The texture ID to set the skull to, type java.lang.String
     * @return          The skull meta with the texture set, type org.bukkit.inventory.meta.SkullMeta
     */
    private static SkullMeta getSkullMetaTexture(SkullMeta meta, String texture) {
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
