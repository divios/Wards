package io.github.divios.wards.events;

import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.wards.Wards;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.observer.ObservablesManager;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Class that is called by WardsManager and subscribes
 * to blockPlaceEvent.
 *
 * Checks if the item placed is a Ward.
 * If it is, marks the new block placed with
 * WARD_META and WARD_UUID with the owner uuid
 */

public class WardPlaceEvent implements IObserver {

    private final WardsManager manager;
    private final ObservablesManager OManager;

    public WardPlaceEvent(WardsManager manager){
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

            String owner = new NBTItem(o.getItemInHand()).getString(Wards.WARD_OWNER);

            utils.setWardMetadata(block, UUID.fromString(owner));

            IntStream.range(0, 40).forEach(i -> {
                Location l2 = l.clone();
                o.getPlayer().spawnParticle(Particle.SPELL_WITCH,
                        l2.add(Math.random(), Math.random(), Math.random()), 1);
            });

            manager.createWard(new Ward.Builder(o.getItemInHand())
                    .setLocation(l)
                    .build());


        }
    }
}
