package io.github.divios.wards.wards;

import io.github.divios.wards.Wards;
import io.github.divios.wards.events.WardInteractEvent;
import io.github.divios.wards.events.WardPlaceEvent;
import io.github.divios.wards.tasks.WardsCooldownTask;
import io.github.divios.wards.tasks.WardsWatchTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;

import java.util.*;

/**
 *
 */

public class WardsManager {

    private static final Wards plugin = Wards.getInstance();
    private static WardsManager instance = null;

    private final Map<Location, Ward> wards = Collections.synchronizedMap(new HashMap<>());
    private final Set<WardType> types = new HashSet<>();

    private WardPlaceEvent wardPlaced;
    private WardInteractEvent wardInteract;

    public static WardsManager getInstance() {
        if (instance == null) { // Check 1
            synchronized (WardsManager.class) {
                if (instance == null) { // Check 2
                    instance = new WardsManager();
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {
        types.addAll(WardsParser.parse());
        wardPlaced = new WardPlaceEvent(instance);
        wardInteract = new WardInteractEvent(instance);
        WardsCooldownTask.load();
        WardsWatchTask.load();
    }

    public Ward getWard(Location l) {
        return wards.get(l);
    }

    public Set<WardType> getWardsTypes() { return Collections.unmodifiableSet(types); }

    public WardType getWardType(String type) {
        return types.stream()
                .filter(type1 -> type1.getId().equals(type))
                .findFirst().get();
    }

    public Map<Location, Ward> getWards() {
        return Collections.unmodifiableMap(wards);
    }

    public void createWard(Ward ward) {
        wards.put(ward.getCenter(), ward);
    }

    public void deleteWard(Location l) {
        Ward removed = wards.remove(l);
        if (removed == null) return;
        removed.destroy();

        Block block = l.getBlock();

        block.setType(Material.AIR);
        block.removeMetadata(Wards.WARD_BLOCK, plugin);
        block.getWorld().spawnParticle(Particle.FLAME, l.clone().add(0.5, 0.5, 0.5), 40);
    }

    public void deleteWard(Ward ward) {
        if (ward == null || ward.getCenter() == null) return;
        deleteWard(ward.getCenter());
    }

    public void clear() {
        wards.clear();
    }
}
