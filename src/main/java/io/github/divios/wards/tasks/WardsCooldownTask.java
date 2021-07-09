package io.github.divios.wards.tasks;

import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.bucket.Bucket;
import io.github.divios.core_lib.bucket.factory.BucketFactory;
import io.github.divios.core_lib.bucket.partitioning.PartitioningStrategies;
import io.github.divios.core_lib.event.SingleSubscription;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.scheduler.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.events.WardPlaceEvent;
import io.github.divios.wards.events.WardRemoveEvent;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Location;
import org.bukkit.Particle;

/**
 * Class that takes care of decreasing the timers
 * of all the wards and also refreshing the timer
 * on their respective inventory
 */

public class WardsCooldownTask {

    private static final Wards plugin = Wards.getInstance();
    private static final WardsManager WManager = WardsManager.getInstance();

    private static boolean loaded = false;
    private static Task task;
    private static Bucket<Location> bucket;
    private static SingleSubscription<WardPlaceEvent> placeE;
    private static SingleSubscription<WardRemoveEvent> removeE;

    @Deprecated
    public static void load() {

        if (loaded) return;
        loaded = true;

        bucket = BucketFactory.newHashSetBucket(20, PartitioningStrategies.lowestSize());

        WManager.getWards().forEach((location, ward) -> bucket.add(location));  // Initial population

        placeE = Events.subscribe(WardPlaceEvent.class)
                .filter(o -> !o.isCancelled())
                .handler(e -> bucket.add(e.getLocation()));

        removeE = Events.subscribe(WardRemoveEvent.class)
                .handler(e -> bucket.remove(e.getWard().getCenter()));

        task = Schedulers.builder()
                .async()
                .after(1)
                .every(1)
                .run(() -> {

                    bucket.asCycle().next().forEach(l -> {

                        Ward ward = WManager.getWard(l);
                        if (ward == null) return;  // Just in case

                        if (ward.getTimer() == -1) return;      // Ignore disabled timer

                        ward.setTimer(ward.getTimer() - 1);
                        //ward.updateInv();

                        if (ward.getTimer() == 0) {
                            Msg.sendMsg(ward.getOwner(), Msg.singletonMsg(Wards.langValues.WARD_EXPIRED)
                                    .add("\\{ward}", ward.getName()).build());
                            utils.cleanBlock(ward.getCenter());
                            ward.getCenter().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                                    ward.getCenter().clone().add(0.5, 0.5, 0.5), 40);
                            Schedulers.sync().run(() -> WManager.deleteWard(ward));
                        }
                    });
                });
    }

    public static void unload() {
        if (!loaded) return;

        loaded = false;
        placeE.unregister();
        removeE.unregister();
        bucket.clear();
        task.close();
    }
}
