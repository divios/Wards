package io.github.divios.wards.wards;

import io.github.divios.wards.Wards;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.observer.ObservablesManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.stream.IntStream;

public class WardPlaced implements IObserver {

    private final WardsManager manager;
    private final ObservablesManager OManager;

    public WardPlaced(WardsManager manager){
        this.manager = manager;
        this.OManager = ObservablesManager.getInstance();

        OManager.sToPlaceEvent(this);
    }

    @Override
    public void update(IObservable observable, Object object) {
        if (observable.getClass().equals(io.github.divios.wards.observer.BlockPlaceEvent.class)) {

            BlockPlaceEvent o = (BlockPlaceEvent) object;
            Block block = o.getBlockPlaced();
            Location l = block.getLocation();

            IntStream.range(0, 40).forEach(i -> {
                Location l2 = l.clone();
                l.getWorld().spawnParticle(Particle.SPELL_WITCH,
                        l2.add(Math.random(), Math.random(), Math.random()), 1);
            });


            manager.createWard(new Ward.Builder(o.getItemInHand())
                    .setLocation(l)
                    .setTimer(30)
                    .build());


        }
    }
}
