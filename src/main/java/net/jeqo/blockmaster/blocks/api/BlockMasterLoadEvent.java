package net.jeqo.blockmaster.blocks.api;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Triggers when everything is loaded within the plugin and the blocks are added
 * to the block registry.
 */
@Getter
public class BlockMasterLoadEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    protected boolean reloaded;

    /**
     *                  Triggers when everything is loaded within the plugin and the blocks are added
     *                  to the block registry.
     * @param reloaded  If the plugin was reloaded, type boolean
     */
    public BlockMasterLoadEvent(boolean reloaded) {
        this.reloaded = reloaded;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
