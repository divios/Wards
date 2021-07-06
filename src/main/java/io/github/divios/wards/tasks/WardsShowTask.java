package io.github.divios.wards.tasks;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.divios.core_lib.bucket.Bucket;
import io.github.divios.core_lib.bucket.factory.BucketFactory;
import io.github.divios.core_lib.bucket.partitioning.PartitioningStrategies;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.ParticleUtils;
import io.github.divios.wards.wards.Ward;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WardsShowTask {

    private static final Wards plugin = Wards.getInstance();
    private static Cache<UUID, Task> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(Wards.configValues.CHUNK_DISPLAY_COOLDOWN, TimeUnit.SECONDS).build();

    public static void generate(Player p, Ward ward) {

        if (cache.asMap().containsKey(p.getUniqueId())) {
            Msg.sendMsg(p, Wards.langValues.WARD_COOLDOWN);
            return;
        }

        int[] ticks = {0};

        Bucket<Block> bucket = BucketFactory.newHashSetBucket(5, PartitioningStrategies.lowestSize());

        bucket.addAll(ward.getRegion().getSurface());

        cache.put(p.getUniqueId(), Task.asyncRepeating(plugin, task -> {

            if (ticks[0] == Wards.configValues.CHUNK_DISPLAY_SECONDS * 4 * 5) {
                task.cancel();
                return;
            }

            bucket.asCycle().next().stream()
                    .filter(block -> block.getLocation().distance(p.getLocation()) < 40)
                    .forEach(block -> {
                        ParticleUtils.spawnParticleShape(p, block.getLocation().add(0, 1, 0));
                    });
            ticks[0]++;

        }, 0, 1));

    }

    public static void reload() {
        cache.invalidateAll();
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(Wards.configValues.CHUNK_DISPLAY_COOLDOWN, TimeUnit.SECONDS).build();
    }

}
