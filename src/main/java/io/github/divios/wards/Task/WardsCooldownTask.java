package io.github.divios.wards.Task;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.WardsManager;

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
            WManager.getWards().parallelStream().forEach(ward -> {
                ward.setTimer(ward.getTimer() - 1);
                ward.getInv().setItem(11, new ItemBuilder(XMaterial.CLOCK)
                        .setName("&a" + FormatUtils.formatTimeOffset(ward.getTimer() * 1000L)));
            });
            WManager.getWards().stream()
                    .filter(ward -> ward.getTimer() <= 0)
                    .forEach(ward -> {
                        utils.sendMsg(ward.getOwner(), "&7Tu Ward ha expirado");
                        Task.syncDelayed(plugin, () -> WManager.deleteWard(ward), 0);
                    });
        }, 20, 20);
    }
}
