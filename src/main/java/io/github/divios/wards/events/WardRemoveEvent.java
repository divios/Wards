package io.github.divios.wards.events;

import io.github.divios.wards.wards.Ward;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WardRemoveEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Player p;
    private final Ward ward;

    public WardRemoveEvent(Player p, Ward ward) {
        this.p = p;
        this.ward = ward;
    }

    public Player getP() {
        return p;
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
