package net.jeqo.blockmaster.listeners;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.blockmaster.BlockMaster;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;

/**
 * Core class for handling listeners
 */
@Setter
@Getter
public class ListenerCore {
    private BlockMaster plugin;
    private ArrayList<Listener> listeners;

    /**
     *                 Creates a new instance of the listener core
     * @param plugin   The plugin to register the listeners to, type net.jeqo.bloons.Bloons
     */
    public ListenerCore(BlockMaster plugin) {
        this.setPlugin(plugin);

        this.setListeners(new ArrayList<>());
    }

    /**
     *                  Adds a listener to the list of listeners
     * @param listener  The listener to add, type org.bukkit.event.Listener
     */
    public void stageListener(Listener listener) {
        this.getListeners().add(listener);
    }

    /**
     * Registers all listeners in the listeners list
     */
    public void registerListeners() {
        for (Listener listener : this.getListeners()) {
            this.getPlugin().getServer().getPluginManager().registerEvents(listener, this.getPlugin());
        }
    }

    /**
     * Unregisters all listeners and clear staged listeners
     */
    public void unregisterListeners() {
        this.setListeners(new ArrayList<>());
        HandlerList.unregisterAll(this.getPlugin());
    }
}
