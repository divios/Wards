package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.core_lib.region.Region;
import io.github.divios.core_lib.region.SpheroidRegion;
import io.github.divios.wards.Wards;
import io.github.divios.wards.observer.BlockInteractEvent;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.observer.ObservablesManager;
import io.github.divios.wards.tasks.WardsShowTask;
import io.github.divios.wards.utils.ParticleUtils;
import io.github.divios.wards.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * This class represents the Ward object itself, this means,
 * the specific block placed representing a ward
 * <p>
 * It also observer the InteractEvent and checks if the location
 * is their saved location. If it is, cancels the event and shows
 * the respective GUI
 */

public class Ward{

    private static final Wards plugin = Wards.getInstance();
    private static final ObservablesManager OManager = ObservablesManager.getInstance();

    private final UUID owner;
    private final String id;  //TODO: implement interface
    private final SpheroidRegion region;
    private int timer;

    private final int hash;             // Hash of the object cached

    private final Set<UUID> acceptedP = new HashSet<>();
    private final Set<Player> onSight = new HashSet<>();

    private final InventoryGUI inv;

    public Ward(UUID owner, Location location, String id, Integer radius, Integer timer) {

        this.owner = owner;
        this.acceptedP.add(owner);
        this.id = id;
        this.region = new SpheroidRegion(location, radius);
        this.timer = 240;

        this.hash = Objects.hash(owner, region.getCenter(), id);

        this.inv = WardInventory.build(this);

    }

    public UUID getOwner() {
        return owner;
    }

    public Location getCenter() {
        return region.getCenter();
    }

    public String getId() {
        return id;
    }

    public SpheroidRegion getRegion() {
        return this.region;
    }

    public double getRadius() {
        return region.getXRadius();
    }

    public Set<Chunk> getChunks() { return region.getChunks(); }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public Inventory getInv() {
        return inv.getInventory();
    }

    public Set<UUID> getAcceptedP() {
        return Collections.unmodifiableSet(acceptedP);
    }

    public Set<Player> getOnSight() {
        return Collections.unmodifiableSet(onSight);
    }

    public void openInv(Player p) {
        inv.open(p);
    }

    public void destroy() {

        new ArrayList<>(inv.getInventory().getViewers())
                .forEach(HumanEntity::closeInventory);
        inv.destroy();

    }

    public void updateOnSight(List<Player> players) {

        onSight.stream()       // Players who exited
                .filter(player -> !players.contains(player))
                .forEach(player -> {
                    acceptedP.forEach(uuid -> Msg.sendMsg(uuid,
                            player.getName() + " &7exited your ward"));

                    acceptedP.forEach(uuid -> {     // remove glow
                        Player permitted = Bukkit.getPlayer(uuid);
                        if (permitted == null) return;
                        ParticleUtils.removeGlow(permitted, player);       // Packets

                    });
                });

        players.stream()        // Players who entered
                .filter(player -> !onSight.contains(player))
                .forEach(player -> {
                    acceptedP.forEach(uuid -> Msg.sendMsg(uuid,
                            player.getName() + " &7entered your ward"));
                });

        onSight.clear();
        onSight.addAll(players);

        onSight.forEach(player -> Task.syncDelayed(plugin, () -> {      // All players on the area

            //player.addPotionEffect(PotionEffectType.GLOWING.createEffect(50, 3));   // effect Deprecated

            acceptedP.forEach(uuid -> {     // add glow
                Player permitted = Bukkit.getPlayer(uuid);
                if (permitted == null) return;
                ParticleUtils.addGlow(permitted, player);       // Packets

            });

        }));

    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ward ward = (Ward) o;
        return owner.equals(ward.getOwner()) &&
                region.getCenter().equals(ward.getCenter()) && id.equals(ward.getId());
    }

    public static class Builder {

        private UUID uuid = null;
        private Location location = null;
        private String id;  //TODO: implement interface
        private Integer radius = 30;
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

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setTimer(Integer timer) {
            this.timer = timer;
            return this;
        }

        public Ward build() {
            Objects.requireNonNull(uuid, "Uuid can't be null");
            Objects.requireNonNull(id, "Id can't not be null");

            return new Ward(this.uuid, this.location, this.id, this.radius, this.timer);
        }
    }
}
