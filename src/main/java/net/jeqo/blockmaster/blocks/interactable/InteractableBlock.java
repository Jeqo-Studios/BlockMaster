package net.jeqo.blockmaster.blocks.interactable;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface InteractableBlock {
    void onInteract(Player player, Block block);
}
