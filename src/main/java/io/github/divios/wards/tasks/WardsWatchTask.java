package io.github.divios.wards.tasks;

import io.github.divios.core_lib.bucket.Bucket;
import io.github.divios.core_lib.bucket.factory.BucketFactory;
import io.github.divios.core_lib.bucket.partitioning.PartitioningStrategies;
import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.core_lib.misc.Task;
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
    private static EventListener<WardPlaceEvent> placeE;
    private static EventListener<WardRemoveEvent> removeE;

    public static void load() {

        if (loaded) return;

        loaded = true;

        bucket = BucketFactory.newHashSetBucket(Wards.configValues.WARD_CHECK_SECONDS,
                PartitioningStrategies.lowestSize());

        WManager.getWards().forEach((location, ward) -> bucket.add(location));  // Initial population

        placeE = new EventListener<>(plugin, WardPlaceEvent.class, e -> {
            if (e.isCancelled()) return;

            bucket.add(e.getLocation());
        });

        removeE = new EventListener<>(plugin, WardRemoveEvent.class, e -> bucket.remove(e.getWard().getCenter()));

        task = Task.asyncRepeating(plugin, () ->

                bucket.asCycle().next().forEach(l -> {

                    Ward ward = WManager.getWard(l);
                    if (ward == null) return;  // Just in case

                    if (ward.getRegion().getLoadedChunks().isEmpty()) return;

                    ward.updateOnSight(Bukkit.getOnlinePlayers().stream()
                            .filter(p -> ward.isInside(p.getLocation()))
                            .filter(player -> !player.getUniqueId().equals(ward.getOwner()))
                            .collect(Collectors.toList()));

                }), 1, 1);
    }

    public static void unload() {

        if (!loaded) return;

        loaded = false;
        placeE.unregister();
        removeE.unregister();
        bucket.clear();
        task.cancel();
    }

    public static void reload() {
        unload();
        load();
    }

}
