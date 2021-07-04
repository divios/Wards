package io.github.divios.wards.tasks;

import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.WardsManager;
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

    public static void load() {
        if (loaded) return;
        loaded = true;

        task = Task.asyncRepeating(plugin, () -> {
            WManager.getWards().forEach( (location, ward) -> {

                        if (ward.getTimer() == -1) return;      // Ignore disabled timer

                        ward.setTimer(ward.getTimer() - 1);
                        ward.updateInv();

                        if (ward.getTimer() == 0) {
                            Msg.sendMsg(ward.getOwner(), Msg.singletonMsg(Wards.langValues.WARD_EXPIRED)
                                    .add("\\{ward}", ward.getName()).build());
                            utils.cleanBlock(ward.getCenter());
                            ward.getCenter().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                                    ward.getCenter().clone().add(0.5, 0.5, 0.5), 40);
                            Task.syncDelayed(plugin, () -> WManager.deleteWard(ward));
                        }

                    });

        }, 20, 20);
    }

    public static void unload() {
        if (!loaded) return;

        loaded = false;
        task.cancel();
    }
}
