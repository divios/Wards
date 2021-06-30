package io.github.divios.wards.wards;

import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.wards.Wards;
import io.github.divios.wards.observer.BlockInteractEvent;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.observer.ObservablesManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * This class represents the Ward object itself, this means,
 * the specific block placed representing a ward
 */

public class Ward implements IObserver {

    private static final Wards plugin = Wards.getInstance();
    private static final ObservablesManager OManager = ObservablesManager.getInstance();

    private final UUID owner;
    private final Location location;
    private final String id;

    public Ward(UUID owner, Location location, String id) {
        this.owner = owner;
        this.location = location;
        this.id = id;

        OManager.sToInteract(this);
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public void destroy() {
        OManager.unToInteract(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, location, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ward ward = (Ward) o;
        return owner.equals(ward.getOwner()) &&
                location.equals(ward.getLocation()) && id.equals(ward.getId());
    }

    @Override
    public void update(IObservable observable, Object object) {

        if (observable.getClass().equals(BlockInteractEvent.class)){
            PlayerInteractEvent o = (PlayerInteractEvent) object;
            Location l = o.getClickedBlock().getLocation();

            if (!location.equals(l)) return;

            InventoryGUI inv = new InventoryGUI(plugin, 27, "&6&lManage Ward ");

            inv.open(o.getPlayer());

        }
    }
}
