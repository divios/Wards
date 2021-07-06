package io.github.divios.wards.events;

import io.github.divios.wards.wards.Ward;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WardInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCanceled = false;

    private final Player p;
    private final Ward ward;

    public WardInteractEvent(Player p, Ward ward) {
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

    @Override
    public boolean isCancelled() {
        return isCanceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCanceled = cancel;
    }
}
