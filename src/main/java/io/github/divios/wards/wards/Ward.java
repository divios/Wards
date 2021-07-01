package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.wards.Wards;
import io.github.divios.wards.observer.BlockInteractEvent;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.observer.ObservablesManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * This class represents the Ward object itself, this means,
 * the specific block placed representing a ward
 */

public class Ward implements IObserver {

    private static final Wards plugin = Wards.getInstance();
    private static final ObservablesManager OManager = ObservablesManager.getInstance();
    private static final WardsManager WManager = WardsManager.getInstance();

    private final UUID owner;
    private final Location location;
    private final String id;  //TODO: implement interface
    private int timer = 30;

    private final InventoryGUI inv = new InventoryGUI(plugin, 27, "&1&lWard Manager");

    public Ward(UUID owner, Location location, String id, Integer timer) {
        this.owner = owner;
        this.location = location;
        this.id = id;
        this.timer = timer;
        createInv();

        OManager.sToInteract(this);
    }

    private void createInv() {
        inv.addButton(ItemButton.create(new ItemBuilder(XMaterial.BARRIER),
                e -> {
                    Optional.ofNullable(Bukkit.getPlayer(owner))
                            .ifPresent(o -> o.sendMessage(FormatUtils.color("&7Removiste tu ward")));
                    WManager.deleteWard(this);
                    // TODO: Give item back to player
                    e.getWhoClicked().closeInventory();
                }), 13);
        inv.setDestroyOnClose(false);
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
        new ArrayList<>(inv.getInventory().getViewers())
                .forEach(HumanEntity::closeInventory);
        inv.destroy();
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
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

            inv.open(o.getPlayer());

        }
    }

    public static class Builder {

        private UUID uuid = null;
        private Location location = null;
        private String id;  //TODO: implement interface
        private Integer timer = null;

        public Builder(UUID uuid) {
            this.uuid = uuid;
        }

        public Builder(Player p) {
            this.uuid = p.getUniqueId();
        }

        public Builder(ItemStack item) {
            this(new NBTItem(item));
        }

        public Builder(NBTItem item) {
            this.uuid = UUID.fromString(item.getString(Wards.WARD_UUID));
            this.id = item.getString(Wards.WARD_ID);
            this.timer = item.getInteger(Wards.WARD_TIMER);

        }

        public Builder setLocation(Location l) {
            this.location = l;
            return this;
        }

        public Builder setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setTimer(Integer timer) {
            this.timer = timer;
            return this;
        }

        public Ward build() {
            Objects.requireNonNull(uuid, "Uuid can't be null");
            Objects.requireNonNull(id, "Id can't not be null");

            return new Ward(this.uuid, this.location, this.id, this.timer);
        }
    }
}
