package io.github.divios.wards.tasks;

import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;

import java.util.stream.Collectors;

public class WardsWatchTask {

    private static final Wards plugin = Wards.getInstance();
    private static final WardsManager WManager = WardsManager.getInstance();

    private static boolean loaded = false;
    private static Task task;

    public static void load() {

        if (loaded) return;

        loaded = true;
        task = Task.asyncRepeating(plugin, () ->

                WManager.getWards().forEach((key, ward) -> {

                    if (ward.getRegion().getChunks().isEmpty()) return;

                    ward.updateOnSight(Bukkit.getOnlinePlayers().stream()
                            .filter(p -> ward.isInside(p.getLocation()))
                            .filter(player -> !player.getUniqueId().equals(ward.getOwner()))
                            .collect(Collectors.toList()));

                }), 30, 30);
    }

    public static void unload() {

        if (!loaded) return;

        loaded = false;
        task.cancel();
    }

}
