package io.github.divios.wards.events;

import io.github.divios.wards.wards.Ward;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WardExpireEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCanceled = false;

    private final Location l;
    private final Ward ward;

    public WardExpireEvent(Location l, Ward ward) {
        this.l = l;
        this.ward = ward;
    }

    public Location getLocation() {
        return l;
    }

    public Ward getWard() {
        return ward;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
