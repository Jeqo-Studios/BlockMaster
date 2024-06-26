package net.jeqo.blockmaster.gui;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.BlockRegistry;
import net.jeqo.blockmaster.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Represents a GUI for the BlockMaster plugin to fetch and use custom blocks
 */
@Getter @Setter
public class BlockMasterGUI {
    /**
     * The saved pages for each player
     */
    public static Map<UUID, Integer> savedPages = new HashMap<>();
    /**
     * The current page of the GUI
     */
    public static int currentPage;
    /**
     * The inventory of the GUI
     */
    private static Inventory inventory;

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
        int customBlocksCount = BlockRegistry.getRegistry().size(), totalPages = getTotalPages();

        // Set the current page
        currentPage = (Math.max(1, Math.min(page, totalPages))); // Set page range to 1 >= page >= totalPages

        // Create the inventory
        String inventoryTitle = Essentials.colorize(" [" + page + "/" + totalPages + "]");
        inventory = Bukkit.createInventory(null, 54, BlockMaster.getInstance().getConfig().getString("menu-title") + inventoryTitle);

        // Fill the inventory with the custom blocks
        for (int invSlot = 0, customBlockIndex = (page - 1) * 45;
             invSlot < 45 && customBlockIndex < customBlocksCount; invSlot++, customBlockIndex++)
            inventory.setItem(invSlot, BlockRegistry.getRegistry().get(customBlockIndex).getItemStack(true));

        // Fill the border with the specified material
        fillBorder(Material.BLACK_STAINED_GLASS_PANE);

        // Add the page related button heads/items
        inventory.setItem(45, BlockMasterGUIItems.getBackButton());
        inventory.setItem(49, BlockMasterGUIItems.getInfoButton(page, totalPages));
        inventory.setItem(53, BlockMasterGUIItems.getNextButton());

        // Open the inventory for the player and add them and the page to the array
        player.openInventory(inventory);
        savedPages.put(player.getUniqueId(), page);
    }

    /**
     *                          Fills the border with the specified material
     * @param fillMaterial      The material to fill the border with, type org.bukkit.Material
     */
    private static void fillBorder(Material fillMaterial) {
        for (int i = (9 * 5); i < (9 * 6); i++) {
            ItemStack item = new ItemStack(fillMaterial);
            ItemMeta meta = item.getItemMeta();

            // Ignore if the meta is null
            if (meta == null) continue;

            meta.setDisplayName(ChatColor.RESET + "");
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }
    }

    /**
     *          Gets the total amount of pages in the GUI based off of the blocks in the registry
     * @return  The total amount of pages in the GUI, type int
     */
    public static int getTotalPages() {
        int cbksCount = BlockRegistry.getRegistry().size(),
                totalPages = cbksCount / (9 * 5);    // 45 -> The max blocks per page
        if (cbksCount % (9 * 5) > 0) ++totalPages;   // extra page for the last items
        return totalPages;
    }
}
