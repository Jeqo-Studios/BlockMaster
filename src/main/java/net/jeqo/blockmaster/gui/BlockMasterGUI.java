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
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

public class BlockMasterGUI {
    public static Map<UUID, Integer> savedPages = new HashMap<>();

    public static void open(@NotNull Player player) {
        open(player, savedPages.getOrDefault(player.getUniqueId(), 1));
    }

    public static void open(@NotNull Player player, int page) {
        int cbksCount = CustomBlock.getRegistry().size(), totalPages = getTotalPages();

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
             invSlot < 45 && cbksIndex < cbksCount; invSlot++, cbksIndex++)
            inv.setItem(invSlot, CustomBlock.getRegistry().get(cbksIndex).getItemStack(true));

        inv.setItem(45, getBackBtn());
        inv.setItem(49, getInfoBtn(page, totalPages));
        inv.setItem(53, getNextBtn());

        player.openInventory(inv);
        savedPages.put(player.getUniqueId(), page);
    }

    public static int getTotalPages() {
        int cbksCount = CustomBlock.getRegistry().size(),
                totalPages = cbksCount / (9 * 5);    // 45 -> The max blocks per page
        if (cbksCount % (9 * 5) > 0) ++totalPages;   // extra page for the last items
        return totalPages;
    }

    private static ItemStack getBackBtn() {
        String texture = "8652e2b936ca8026bd28651d7c9f2819d2e923697734d18dfdb13550f8fdad5f";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTextureByB64(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Utils.colorize("&aBack"));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getInfoBtn(int page, int totalPages) {
        String texture = "2e3f50ba62cbda3ecf5479b62fedebd61d76589771cc19286bf2745cd71e47c6";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTextureByB64(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Utils.colorize("&aPage " + page + "/" + totalPages));
        meta.setLore(Collections.singletonList(Utils.colorize("&a&lLeft Click &r&8Go back to page 1")));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getNextBtn() {
        String texture = "2a3b8f681daad8bf436cae8da3fe8131f62a162ab81af639c3e0644aa6abac2f";
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        assert item.getItemMeta() != null;
        SkullMeta meta = getSkullMetaTextureByB64(((SkullMeta) item.getItemMeta()), texture);
        meta.setDisplayName(Utils.colorize("&aNext"));
        item.setItemMeta(meta);
        return item;
    }

    private static SkullMeta getSkullMetaTextureByB64(@NotNull SkullMeta meta, @NotNull String texture) {
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
