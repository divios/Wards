package io.github.divios.wards.tasks;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.Ward;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WardsShowTask {

    private static final Wards plugin = Wards.getInstance();
    private static final Cache<UUID, Task> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS).build();

    public static void generate(Player p, Ward ward) {

        if (cache.asMap().containsKey(p.getUniqueId())) {
            Msg.sendMsg(p, "&7A cooldown is active, wait a few seconds");
            return;
        }

        int[] ticks = {0};

        Set<Block> surface = ward.getRegion().getSurface();

        cache.put(p.getUniqueId(), Task.asyncRepeating(plugin, task -> {

           if (ticks[0] == 60) {
               task.cancel();
               return;
           }

            surface.forEach(block -> {
                p.spawnParticle(Particle.REDSTONE,
                        block.getLocation().add(0, 1, 0), 1,
                        new Particle.DustOptions(Color.NAVY, 1));
            });
            ticks[0]++;

        }, 0, 5));

    }

}
