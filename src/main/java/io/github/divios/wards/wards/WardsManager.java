package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.observer.BlockInteractEvent;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.observer.ObservablesManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.*;
import java.util.stream.IntStream;

public class WardsManager {

    private static final Wards plugin = Wards.getInstance();

    private static WardsManager instance = null;

    private final Set<Ward> wards = Collections.synchronizedSet(new HashSet<>());
    private WardPlaced wardPlaced;
    private Task task;

    public static WardsManager getInstance() {
        if (instance == null) {
            instance = new WardsManager();
            instance.wardPlaced = new WardPlaced(instance);
            instance.cooldownTask();
        }
        return instance;
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
                .ifPresent(ward -> {
                    Bukkit.getPlayer(ward.getOwner()).sendMessage(
                            FormatUtils.color("&7Tu Ward ha desaparecido"));
                    deleteWard(ward);
                });
    }

    public void deleteWard(Ward ward) {
        wards.remove(ward);
        ward.destroy();

        Block block = ward.getLocation().getBlock();
        Location l = ward.getLocation();

        block.setType(Material.AIR);
        block.removeMetadata(Wards.WARD_BLOCK, plugin);
        block.getWorld().spawnParticle(Particle.FLAME, l.clone().add(0.5, 0.5, 0.5), 40);
    }

    public void clear() {
        wards.clear();
    }

    private void cooldownTask() {

        instance.task = Task.asyncRepeating(plugin, () -> {
            wards.forEach(ward -> {
                ward.setTimer(ward.getTimer() - 1);
                ward.getInv().setItem(11, new ItemBuilder(XMaterial.CLOCK)
                        .setName("&a" + FormatUtils.formatTimeOffset(ward.getTimer() * 1000L)));
            });
            wards.stream()
                    .filter(ward -> ward.getTimer() <= 0)
                    .forEach(ward -> Task.syncDelayed(plugin, () -> deleteWard(ward), 0));
        }, 20, 20);
    }
}
