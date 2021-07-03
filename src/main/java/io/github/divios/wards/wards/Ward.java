package io.github.divios.wards.wards;

import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.core_lib.region.SpheroidRegion;
import io.github.divios.wards.Wards;
import io.github.divios.wards.regions.ChunkRegion;
import io.github.divios.wards.regions.RegionI;
import io.github.divios.wards.utils.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * This class represents the Ward object itself, this means,
 * the specific block placed representing a ward
 * <p>
 * It also observer the InteractEvent and checks if the location
 * is their saved location. If it is, cancels the event and shows
 * the respective GUI
 */

public class Ward {

    private static final Wards plugin = Wards.getInstance();
    private static final WardsManager WManager = WardsManager.getInstance();

    private final UUID owner;
    private final WardType type;
    private final RegionI region;
    private int timer;

    private final int hash;             // Hash of the object cached

    private final Set<UUID> acceptedP = new HashSet<>();
    private final Set<Player> onSight = new HashSet<>();

    private final InventoryGUI inv;

    private Ward(UUID owner, WardType type, Integer timer, RegionI region, Set<UUID> acceptedP) {

        this.owner = owner;
        this.acceptedP.add(owner);
        this.type = type;
        this.region = region;
        this.timer = timer;

        this.acceptedP.addAll(acceptedP);

        this.hash = Objects.hash(region.getCenter(), type);

        this.inv = WardInventory.build(this);

    }

    public UUID getOwner() {
        return owner;
    }

    public Location getCenter() {
        return region.getCenter();
    }

    public WardType getType() {
        return type;
    }

    public RegionI getRegion() {
        return this.region;
    }

    public boolean isInside(Location l) {
        return region.isInside(l);
    }

    public boolean isInside(Player p) {
        return region.isInside(p.getLocation());
    }

    public Set<Chunk> getChunks() {
        return region.getChunks();
    }

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

    public ItemStack buildItem() {
        NBTItem item = new NBTItem(type.buildItem());

        item.setInteger(Wards.WARD_TIMER, timer);
        item.setString(Wards.WARD_OWNER, owner.toString());
        item.setObject(Wards.WARD_ACCEPTED, acceptedP);

        return item.getItem();
    }

    public void destroy() {

        new ArrayList<>(inv.getInventory().getViewers())
                .forEach(HumanEntity::closeInventory);

        onSight.forEach(player ->       // All players on the area

                acceptedP.forEach(uuid -> {     // add glow
                    Player permitted = Bukkit.getPlayer(uuid);
                    if (permitted == null) return;
                    ParticleUtils.removeGlow(permitted, player);       // Packets

                }));

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

        onSight.forEach(player ->       // All players on the area

            //player.addPotionEffect(PotionEffectType.GLOWING.createEffect(50, 3));   // effect Deprecated
            acceptedP.forEach(uuid -> {     // add glow
                Player permitted = Bukkit.getPlayer(uuid);
                if (permitted == null) return;
                ParticleUtils.addGlow(permitted, player);       // Packets

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
                region.getCenter().equals(ward.getCenter()) && type.equals(ward.getType());
    }

    public static class Builder {

        private UUID uuid = null;
        private Location location = null;
        private WardType type = null;
        private Integer timer = null;
        private Set<UUID> accepted = null;

        public Builder(Player p) {
            this.uuid = p.getUniqueId();
            this.location = p.getLocation();
        }

        public Builder(UUID uuid) {
            this.uuid = uuid;
            Optional.ofNullable(Bukkit.getPlayer(uuid))
                    .ifPresent(player -> this.location = player.getLocation());
        }

        public Builder(ItemStack item) {
            this(new NBTItem(item));
        }

        public Builder(NBTItem item) {
            this.uuid = UUID.fromString(item.getString(Wards.WARD_OWNER));
            this.type = WManager.getWardType(item.getString(Wards.WARD_ID));
            this.timer = item.hasKey(Wards.WARD_TIMER) ?
                    item.getInteger(Wards.WARD_TIMER) :
                    type.getTime();
            this.accepted = item.getObject(Wards.WARD_ACCEPTED, Set.class);
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
            this.type = WManager.getWardType(id);
            return this;
        }

        public Builder setId(WardType type) {
            this.type = type;
            return this;
        }

        public Builder setTimer(Integer timer) {
            this.timer = timer;
            return this;
        }

        public Builder setAccepted(Set<UUID> accepted) {
            this.accepted = accepted;
            return this;
        }

        public Ward build() {
            Objects.requireNonNull(uuid, "Uuid can't be null");
            Objects.requireNonNull(type, "Type can't not be null");
            Objects.requireNonNull(location, "Location cannot be null");

            if (timer == null)
                timer = type.getTime();

            if (accepted == null) accepted = Collections.emptySet();

            return new Ward(
                    this.uuid,
                    this.type,
                    this.timer,
                    type.getRegion(location),
                    accepted);
        }
    }
}
