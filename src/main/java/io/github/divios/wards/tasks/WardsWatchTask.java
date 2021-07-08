package io.github.divios.wards.tasks;

import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.bucket.Bucket;
import io.github.divios.core_lib.bucket.factory.BucketFactory;
import io.github.divios.core_lib.bucket.partitioning.PartitioningStrategies;
import io.github.divios.core_lib.event.SingleSubscription;
import io.github.divios.core_lib.scheduler.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.events.WardPlaceEvent;
import io.github.divios.wards.events.WardRemoveEvent;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.stream.Collectors;

public class WardsWatchTask {

    private static final Wards plugin = Wards.getInstance();
    private static final WardsManager WManager = WardsManager.getInstance();

    private static boolean loaded = false;
    private static Task task;
    private static Bucket<Location> bucket;
    private static SingleSubscription<WardPlaceEvent> placeE;
    private static SingleSubscription<WardRemoveEvent> removeE;

    public static void load() {

        if (loaded) return;

        loaded = true;

        bucket = BucketFactory.newHashSetBucket(Wards.configValues.WARD_CHECK_SECONDS,
                PartitioningStrategies.lowestSize());

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

                        if (ward.getRegion().getLoadedChunks().isEmpty()) return;

                        ward.updateOnSight(Bukkit.getOnlinePlayers().stream()
                                .filter(p -> ward.isInside(p.getLocation()))
                                .filter(player -> !player.getUniqueId().equals(ward.getOwner()))
                                .collect(Collectors.toList()));
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

    public static void reload() {
        unload();
        load();
    }

}
