package io.github.divios.wards.Task;

import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import java.util.Arrays;

public class WardsCheckEvent {

    private static final Wards plugin = Wards.getInstance();
    private static final WardsManager WManager = WardsManager.getInstance();

    private static boolean loaded = false;
    private static Task task;

    public static void load() {
        if (loaded) return;

        loaded = true;
        task = Task.asyncRepeating(plugin, () ->

                WManager.getWards().parallelStream()
                .forEach(ward -> {

                    Location l = ward.getLocation();
                    int radius = ward.getRadius();

                    Bukkit.getOnlinePlayers().stream()
                            .filter(p -> p.getLocation().distance(l) <= radius)
                            //.filter(player -> !player.getUniqueId().equals(ward.getOwner()))
                            .forEach(player -> {
                                player.sendMessage(FormatUtils.color("&7Estas en la zona"));
                            });

                }), 40, 40);
    }

}
