package io.github.divios.wards.utils;

import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtils {

    private final static Wards plugin = Wards.getInstance();

    public static void circleParticle(Location l, Particle particle) {

        final int[] ticks = {0};


        Task.syncRepeating(plugin, task -> {


            ticks[0]++;
        }, 5, 5);

    }

}
