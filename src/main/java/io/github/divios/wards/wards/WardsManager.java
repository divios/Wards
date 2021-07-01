package io.github.divios.wards.wards;

import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.wards.Task.WardsCheckEvent;
import io.github.divios.wards.Task.WardsCooldownTask;
import io.github.divios.wards.Wards;
import io.github.divios.wards.events.WardPlacedEvent;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.*;

/**
 *
 */

public class WardsManager {

    private static final Wards plugin = Wards.getInstance();
    private static WardsManager instance = null;

    private final Set<Ward> wards = Collections.synchronizedSet(new HashSet<>());
    private WardPlacedEvent wardPlaced;

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
        wardPlaced = new WardPlacedEvent(instance);
        WardsCooldownTask.load();
        WardsCheckEvent.load();
    }

    public Set<Ward> getWards() {
        return Collections.unmodifiableSet(wards);
    }

    public void createWard(Ward ward) {
        wards.add(ward);
    }

    public void deleteWard(Location l) {
        wards.stream()
                .filter(ward -> ward.getLocation().equals(l))
                .findFirst()
                .ifPresent(ward ->
                        Optional.ofNullable(Bukkit.getPlayer(ward.getOwner()))
                        .ifPresent(player -> {
                            player.sendMessage(
                                    FormatUtils.color("&7Tu Ward ha desaparecido"));
                            deleteWard(ward);
                        }));
    }

    public void deleteWard(Ward ward) {
        wards.remove(ward);
        ward.destroy();

        Block block = ward.getLocation().getBlock();
        Location l = ward.getLocation();

        Optional.ofNullable(Bukkit.getPlayer(ward.getOwner()))
                .ifPresent(player -> {
                    player.sendMessage(
                            FormatUtils.color("&7Tu Ward ha desaparecido"));
                });

        block.setType(Material.AIR);
        block.removeMetadata(Wards.WARD_BLOCK, plugin);
        block.getWorld().spawnParticle(Particle.FLAME, l.clone().add(0.5, 0.5, 0.5), 40);
    }

    public void clear() {
        wards.clear();
    }
}
