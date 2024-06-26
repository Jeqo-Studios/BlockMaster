package net.jeqo.blockmaster.blocks.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BlockMasterLoadEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    protected boolean reloaded;

    public BlockMasterLoadEvent(boolean reloaded) {
        this.reloaded = reloaded;
    }

    public boolean isReloaded() {
        return reloaded;
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
