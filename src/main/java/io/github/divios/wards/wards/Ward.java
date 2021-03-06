package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XSound;
import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.LocationUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.regions.RegionI;
import io.github.divios.wards.utils.ParticleUtils;
import io.github.divios.wards.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

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

    private String name;
    private final UUID owner;
    private final WardType type;
    private final RegionI region;
    private Long time;

    private final int hash;             // Hash of the object cached

    private final Set<UUID> trusted = new HashSet<>();
    private final Set<UUID> muted = new HashSet<>();
    private final Set<Player> onSight = new HashSet<>();

    private final WardsMenu inv;

    private Ward(String name, UUID owner, WardType type, Long timer, RegionI region, Set<UUID> acceptedP, Set<UUID> muted) {

        this.name = name;
        this.owner = owner;
        this.trusted.add(owner);
        this.type = type;
        this.region = region;

        this.time = timer;

        this.trusted.addAll(acceptedP);
        this.muted.addAll(muted);

        this.hash = Objects.hash(region.getCenter(), type);

        this.inv = new WardsMenu(this);

    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getCenter() {
        return region.getCenter().clone();
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

    public long getTimer() {
        return time;
    }

    public void setTimer(long timer) {
        this.time = timer;
    }

    public Set<UUID> getTrusted() {
        return Collections.unmodifiableSet(trusted);
    }

    public boolean addTrusted(UUID uuid) { return trusted.add(uuid); }

    public boolean removeTrusted(UUID uuid) { return trusted.remove(uuid); }

    public Set<UUID> getMuted() { return Collections.unmodifiableSet(muted); }

    public boolean addmuted(Player p) { return muted.add(p.getUniqueId()); }

    public boolean removeMuted(Player p) { return muted.remove(p.getUniqueId()); }

    public Set<Player> getOnSight() {
        return Collections.unmodifiableSet(onSight);
    }

    public void openInv(Player p) {
        inv.show(p);
    }

    public ItemStack buildItem() {

        NBTItem item = new NBTItem(type.buildItem());

        item.setString(Wards.WARD_NAME, name);
        item.setLong(Wards.WARD_TIMER, time);
        item.setString(Wards.WARD_OWNER, owner.toString());
        item.setObject(Wards.WARD_ACCEPTED, trusted.stream().map(UUID::toString).collect(Collectors.toList()));
        item.setObject(Wards.WARD_MUTED, muted.stream().map(UUID::toString).collect(Collectors.toList()));

        return item.getItem();
    }

    public void destroy() {

        inv.destroy();

        onSight.forEach(player ->       // All players on the area

                trusted.forEach(uuid -> {     // add glow
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
                    trusted.stream().filter(uuid -> !muted.contains(uuid)).forEach(uuid -> {

                        Player permitted = Bukkit.getPlayer(uuid);      // Remove Glow
                        if (permitted == null) return;

                        Msg.sendMsg(permitted, Msg.singletonMsg(Wards.configManager.getLangValues().WARD_EXITED)
                                .add("\\{player}", player.getName())
                                .add("\\{ward}", name).build());
                        utils.sendSound(permitted, XSound.BLOCK_BELL_USE);

                        ParticleUtils.removeGlow(permitted, player);       // Packets
                    });

                });

        players.stream()        // Players who entered
                .filter(player -> !onSight.contains(player))
                .forEach(player -> {
                    trusted.stream().filter(uuid -> !muted.contains(uuid)).forEach(uuid -> {
                        Msg.sendMsg(uuid, Msg.singletonMsg(Wards.configManager.getLangValues().WARD_ENTERED)
                                .add("\\{player}", player.getName())
                                .add("\\{ward}", name).build());
                        utils.sendSound(uuid, XSound.BLOCK_BELL_USE);
                    });
                });

        onSight.clear();
        onSight.addAll(players);

        onSight.forEach(player ->       // All players on the area

            //player.addPotionEffect(PotionEffectType.GLOWING.createEffect(50, 3));   // effect Deprecated
            trusted.forEach(uuid -> {     // add glow
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


    public static Builder builder(String p) {
        return new Builder(p);
    }

    public static Builder builder(Player p) {
        return new Builder(p);
    }

    public static Builder builder(UUID uuid) {
        return new Builder(uuid);
    }

    public static Builder builder(ItemStack item) {
        return new Builder(item);
    }

    public static Builder builder(NBTItem item) {
        return new Builder(item);
    }

    public static class Builder {

        private String name = null;
        private UUID uuid = null;
        private Location location = null;
        private WardType type = null;
        private Long timer = null;
        private Set<UUID> accepted = null;
        private Set<UUID> muted = null;

        public Builder(Player p) {
            Objects.requireNonNull(p, "Player cannot be null");
            this.uuid = p.getUniqueId();
            this.location = p.getLocation();
        }

        public Builder(UUID uuid) {
            this.uuid = uuid;
            Optional.ofNullable(Bukkit.getPlayer(uuid))
                    .ifPresent(player -> this.location = player.getLocation());
        }

        public Builder(String uuid) {
            this(UUID.fromString(uuid));
        }

        public Builder(ItemStack item) {
            this(new NBTItem(item));
        }

        public Builder(NBTItem item) {
            this.name = item.hasKey(Wards.WARD_NAME) ? item.getString(Wards.WARD_NAME):null;
            this.uuid = UUID.fromString(item.getString(Wards.WARD_OWNER));
            this.type = WManager.getWardType(item.getString(Wards.WARD_ID));
            this.timer = item.hasKey(Wards.WARD_TIMER) ?
                    item.getLong(Wards.WARD_TIMER) :
                    type.getTime();
            setAccepted(item.getObject(Wards.WARD_ACCEPTED, List.class));
            setMuted(item.getObject(Wards.WARD_MUTED, List.class));
        }

        public Builder setLocation(Location l) {
            this.location = l;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
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

        public Builder setTimer(Long timer) {
            this.timer = timer;
            return this;
        }

        public Builder setMuted(Set<UUID> muted) {
            if (muted == null) return this;
            this.muted = muted;
            return this;
        }

        public Builder setMuted(List<String> muted) {
            if (muted == null) return this;
            return setMuted(muted.stream().map(UUID::fromString).collect(Collectors.toSet()));
        }


        public Builder setAccepted(Set<UUID> accepted) {
            if (accepted == null) return this;
            this.accepted = accepted;
            return this;
        }

        public Builder setAccepted(List<String> accepted) {
            if (accepted == null) return this;
            return setAccepted(accepted.stream().map(UUID::fromString).collect(Collectors.toSet()));
        }

        public Ward build() {
            Objects.requireNonNull(uuid, "Uuid cannot be null");
            Objects.requireNonNull(type, "Type cannot be null");
            Objects.requireNonNull(location, "Location cannot be null");

            if (name == null) {
                name = FormatUtils.color("&f" + type.getId() + " " + LocationUtils.toString(location));
            }

            if (timer == null)
                timer = type.getTime();

            if (accepted == null) accepted = Collections.emptySet();
            if (muted == null) muted  = Collections.emptySet();

            return new Ward(
                    this.name,
                    this.uuid,
                    this.type,
                    this.timer,
                    type.getRegion(location),
                    accepted,
                    muted);
        }
    }
}
