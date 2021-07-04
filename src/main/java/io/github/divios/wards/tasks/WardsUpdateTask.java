package io.github.divios.wards.tasks;

import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.file.jsonDatabase;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.WardsManager;

public class WardsUpdateTask {

    private static final Wards plugin = Wards.getInstance();
    private static final WardsManager WManager = WardsManager.getInstance();

    private static boolean loaded = false;
    private static Task task;

    public static void load(jsonDatabase database) {
        if (loaded) return;
        loaded = true;

        task = Task.asyncRepeating(plugin, () -> {

            long startTime = System.nanoTime();
            plugin.getLogger().info("Saving database...");

            utils.clearUpFile(database.getFile());
            database.serialize(WManager.getWards().values());

            long elapsedTime = System.nanoTime() - startTime;
            plugin.getLogger().info("Database saved correctly in " + elapsedTime/1000000 + " ms");

        }, 24000, 24000);

    }

    public static void unload() {
        if(!loaded) return;

        loaded = false;
        task.cancel();
    }

}
