package io.github.divios.wards.wards;

import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.file.jsonDatabase;
import io.github.divios.wards.tasks.WardsCooldownTask;
import io.github.divios.wards.tasks.WardsUpdateTask;
import io.github.divios.wards.tasks.WardsWatchTask;
import io.github.divios.wards.utils.utils;
import org.bukkit.Location;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */

public class WardsManager {

    private static final Wards plugin = Wards.getInstance();
    private static WardsManager instance = null;

    private final Map<Location, Ward> wards = new ConcurrentHashMap<>();
    private final Set<WardType> types = new HashSet<>();

    private jsonDatabase database;

    public static WardsManager getInstance() {
        if (instance == null) { // Check 1
            synchronized (WardsManager.class) {
                if (instance == null) { // Check 2
                    instance = new WardsManager();
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {

        types.addAll(WardsParser.parse());

        database = new jsonDatabase(new File(plugin.getDataFolder() + File.separator + "data.json"));

        Task.asyncDelayed(plugin, () -> {

            long startTime = System.nanoTime();
            plugin.getLogger().info("Loading database...");

            database.deserialize().forEach(ward -> {
                if (ward == null) return;

                wards.put(ward.getCenter(), ward);
                utils.setWardsMetadata(ward.getCenter(), ward.getOwner());
            });

            long elapsedTime = System.nanoTime() - startTime;
            plugin.getLogger().info("Database loaded correctly in " + elapsedTime/1000000 + " ms");

            WardsCooldownTask.load();
            WardsWatchTask.load();
            WardsUpdateTask.load(database);

        });

    }

    public Ward getWard(Location l) {
        return wards.get(l);
    }

    public Set<WardType> getWardsTypes() { return Collections.unmodifiableSet(types); }

    public WardType getWardType(String type) {
        return types.stream()
                .filter(type1 -> type1.getId().equals(type))
                .findFirst().orElse(null);
    }

    public Map<Location, Ward> getWards() {
        return Collections.unmodifiableMap(wards);
    }

    public void createWard(Ward ward) {
        wards.put(ward.getCenter(), ward);
    }

    public void deleteWard(Location l) {
        Ward removed = wards.remove(l);
        if (removed == null) return;
        removed.destroy();
    }

    public void deleteWard(Ward ward) {
        if (ward == null || ward.getCenter() == null) return;
        deleteWard(ward.getCenter());
    }

    public void clear() {
        wards.clear();
    }

    public void saveWards() {

        plugin.getLogger().info("Saving database...");
        utils.clearUpFile(database.getFile());
        database.serialize(wards.values());
        plugin.getLogger().info("Database saved correctly");
    }

    public void reload() {
        WardsCooldownTask.unload();
        WardsWatchTask.unload();
        WardsUpdateTask.unload();

        saveWards();

        types.clear();
        wards.forEach((location, ward) -> ward.destroy());
        wards.clear();

        init();
    }

    public void destroy() {
        WardsCooldownTask.unload();
        WardsWatchTask.unload();
        WardsUpdateTask.unload();

        saveWards();
    }

}
