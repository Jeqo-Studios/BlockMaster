package net.jeqo.blockmaster.blocks.interactable;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Represents the data of an interactable block
 */
public interface InteractableBlock {
    /**
     *                  What happens on a block interaction of a custom block
     * @param player    The player interacting with the block
     * @param block     The block being interacted with
     */
    void onInteract(Player player, Block block);
}
