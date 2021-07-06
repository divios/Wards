package io.github.divios.wards.events;

import io.github.divios.wards.wards.WardType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Class that is called by WardsManager and subscribes
 * to blockPlaceEvent.
 *
 * Checks if the item placed is a Ward.
 * If it is, marks the new block placed with
 * WARD_META and WARD_UUID with the owner uuid
 */

public class WardPlaceEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCanceled = false;


    private final Player p;
    private final Location l;
    private final WardType type;

    public WardPlaceEvent(Player p, Location l, WardType type) {
        this.p = p;
        this.l = l;
        this.type = type;
    }

    public Player getP() {
        return p;
    }

    public Location getLocation() {
        return l;
    }

    public WardType getType() {
        return type;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCanceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCanceled = cancel;
    }
}
