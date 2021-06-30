package io.github.divios.wards.wards;

import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.observer.BlockInteractEvent;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.observer.ObservablesManager;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.*;

public class WardsManager implements IObserver{

    private static final Wards plugin = Wards.getInstance();
    private static final ObservablesManager OManager = ObservablesManager.getInstance();

    private static WardsManager instance = null;

    private final Map<Ward, Integer> wards = new Hashtable<>();
    private Task task;

    public static WardsManager getInstance() {
        if (instance == null) {
            instance = new WardsManager();
            OManager.sToPlaceEvent(instance);
            instance.cooldownTask();
        }
        return instance;
    }

    public Map<Ward, Integer> getWards() {
        return Collections.unmodifiableMap(wards);
    }

    public void createWard(Ward ward, int time) { wards.put(ward, time); }

    public void createWard(Ward ward) { createWard(ward, 12000); }

    public void deleteWard(Ward ward) {
        wards.remove(ward);
        ward.destroy();
    }

    public void clear() { wards.clear(); }

    private void cooldownTask() {

        instance.task = Task.asyncRepeating(plugin, () -> {
            instance.wards.replaceAll((ward, integer) -> integer - 1);
            instance.wards.forEach((ward, integer) -> {
                Block block = ward.getLocation().getBlock();
                Location l = block.getLocation();

                if (integer > 0) return;
                instance.deleteWard(ward);

                block.setType(Material.AIR);
                block.getWorld().spawnParticle(Particle.CRIT, l, 5);
                block.getWorld().playEffect(l, Effect.ANVIL_BREAK, 0, 0);
            });
        }, 20, 20);
    }


    @Override
    public void update(IObservable observable, Object object) {
        if (observable.getClass().equals(io.github.divios.wards.observer.BlockPlaceEvent.class)) {

            BlockPlaceEvent o = (BlockPlaceEvent) object;
            Block block = o.getBlockPlaced();

            createWard(new Ward(o.getPlayer().getUniqueId(),
                    block.getLocation(),
                    block.getMetadata(Wards.WARD_BLOCK).get(0).asString()));
        }
    }
}
