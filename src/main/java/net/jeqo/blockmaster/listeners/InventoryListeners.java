package net.jeqo.blockmaster.listeners;

import net.jeqo.blockmaster.BlockMaster;
import net.jeqo.blockmaster.blocks.custom.CustomBlock;
import net.jeqo.blockmaster.gui.BlockMasterGUI;
import net.jeqo.blockmaster.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryListeners implements Listener {

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(BlockMaster.getInstance().getConfig().getString("menu-title"))) return;
        event.setCancelled(true);

        Inventory inv = event.getClickedInventory();
        if (!Objects.equals(inv, event.getView().getTopInventory())) return;
        int slot = event.getSlot();
        if (slot < 0) return;
        ItemStack item = inv.getItem(slot);
        if (ItemUtils.isAirOrNull(item)) return;

        CustomBlock cb = CustomBlock.getCustomBlockByItem(item);
        Player player = (Player) event.getView().getPlayer();

        if (cb != null && slot < 45) {
            item = cb.getItemStack();
            if (event.getClick().isShiftClick()) item.setAmount(64);
            player.getInventory().addItem(item);
            return;
        }

        int page = BlockMasterGUI.savedPages.getOrDefault(player.getUniqueId(), 1);
        switch (slot) {
            case 45:
                BlockMasterGUI.open(player, page - 1);
                break;
            case 49:
                if (!event.isShiftClick()) break;
                BlockMasterGUI.open(player, 1);
                break;
            case 53:
                BlockMasterGUI.open(player, page + 1);
                break;
        }
    }
}
