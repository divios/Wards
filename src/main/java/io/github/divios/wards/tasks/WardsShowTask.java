package io.github.divios.wards.tasks;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.divios.core_lib.Schedulers;
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
import java.util.concurrent.atomic.AtomicInteger;

public class WardsShowTask {

    private static final Wards plugin = Wards.getInstance();
    private static Cache<UUID, Player> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(Wards.configValues.CHUNK_DISPLAY_COOLDOWN, TimeUnit.SECONDS).build();

    public static void generate(Player p, Ward ward) {

        if (cache.asMap().containsKey(p.getUniqueId())) {
            Msg.sendMsg(p, Wards.langValues.WARD_COOLDOWN);
            return;
        }

        cache.put(p.getUniqueId(), p);
        Bucket<Block> bucket = BucketFactory.newHashSetBucket(5, PartitioningStrategies.lowestSize());
        bucket.addAll(ward.getRegion().getSurface());

        AtomicInteger counter = new AtomicInteger(0);
        Schedulers.builder()
                .sync()
                .every(1)
                .consume((task) -> {

                    if (counter.get() >= Wards.configValues.CHUNK_DISPLAY_SECONDS * 4 * 5)
                        task.stop();

                    bucket.asCycle().next().stream()
                            .filter(block -> block.getLocation().distance(p.getLocation()) < 40)
                            .forEach(block ->
                                    ParticleUtils.spawnParticleShape(p,
                                            block.getLocation().add(0, 1, 0)));

                    counter.incrementAndGet();
                });
    }

    public static void reload() {
        cache.invalidateAll();
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(Wards.configValues.CHUNK_DISPLAY_COOLDOWN, TimeUnit.SECONDS).build();
    }

}
