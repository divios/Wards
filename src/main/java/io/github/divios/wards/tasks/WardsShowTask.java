package io.github.divios.wards.tasks;

import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.Ward;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class WardsShowTask {

    private static final Wards plugin = Wards.getInstance();
    private static final HashMap<UUID, Task> executing = new HashMap<>();

    public static void generate(Player p, Ward ward) {

        if (executing.containsKey(p.getUniqueId())) {
            Msg.sendMsg(p, "&7You can only see one ward area at a time");
            return;
        }

        int[] ticks = {0};

        executing.put(p.getUniqueId(), Task.syncRepeating(plugin, task -> {

           if (ticks[0] == 12) {
               task.cancel();
               executing.remove(p.getUniqueId());
               return;
           }

            ward.getRegion().getSurface().forEach(block -> {
                p.spawnParticle(Particle.REDSTONE,
                        block.getLocation().add(0, 1, 0), 1,
                        new Particle.DustOptions(Color.NAVY, 1));
            });
            ticks[0]++;

        }, 0, 20));

    }

}
