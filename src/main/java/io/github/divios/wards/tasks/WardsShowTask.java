package io.github.divios.wards.tasks;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.scheduler.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.ParticleUtils;
import io.github.divios.wards.wards.Ward;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WardsShowTask {

    private static final Wards plugin = Wards.getInstance();
    private static Cache<UUID, Task> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(Wards.configManager.getConfigValues().CHUNK_DISPLAY_COOLDOWN, TimeUnit.SECONDS).build();

    public static void generate(Player p, Ward ward) {

        if (cache.asMap().containsKey(p.getUniqueId())) {
            Msg.sendMsg(p, Wards.configManager.getLangValues().WARD_COOLDOWN);
            return;
        }

        AtomicInteger counter = new AtomicInteger(0);
        cache.put(p.getUniqueId(), Schedulers.builder()
                .async()
                .every(20)
                .consume((task) -> {

                    if (counter.get() >= Wards.configManager.getConfigValues().CHUNK_DISPLAY_SECONDS)
                        task.stop();

                    ward.getRegion().getSurface().stream()
                            .filter(block -> block.getLocation().distance(p.getLocation()) < 40)
                            .forEach(block ->
                                    ParticleUtils.spawnParticleShape(p,
                                            block.getLocation().add(0, 1, 0)));

                    counter.incrementAndGet();
                }));
    }

    public static void unloadAll() {
        cache.asMap().values().forEach(Task::stop);
        cache.invalidateAll();
    }

}
