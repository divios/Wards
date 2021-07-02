package io.github.divios.wards.Task;

import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.stream.Collectors;

public class WardsCheckEvent {

    private static final Wards plugin = Wards.getInstance();
    private static final WardsManager WManager = WardsManager.getInstance();

    private static boolean loaded = false;
    private static Task task;

    public static void load() {
        if (loaded) return;

        loaded = true;
        task = Task.asyncRepeating(plugin, () ->

                WManager.getWards()
                .forEach(ward -> {

                    Location l = ward.getLocation();
                    int radius = ward.getRadius();

                    ward.updateOnSight(Bukkit.getOnlinePlayers().stream()
                            .filter(p -> p.getLocation().distance(l) <= radius)
                            .filter(player -> !player.getUniqueId().equals(ward.getOwner()))
                            .collect(Collectors.toList()));

                }), 40, 40);
    }

}
