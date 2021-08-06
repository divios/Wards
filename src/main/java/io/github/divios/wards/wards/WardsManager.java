package io.github.divios.wards.wards;

import io.github.divios.core_lib.Schedulers;
import io.github.divios.wards.Wards;
import io.github.divios.wards.file.jsonDatabase;
import io.github.divios.wards.tasks.WardsCooldownTask;
import io.github.divios.wards.tasks.WardsShowTask;
import io.github.divios.wards.tasks.WardsUpdateTask;
import io.github.divios.wards.tasks.WardsWatchTask;
import io.github.divios.wards.utils.utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 */

public class WardsManager {

    private static final Wards plugin = Wards.getInstance();
    private static WardsManager instance = null;

    private final Map<Location, Ward> wards;
    private final Set<WardType> types;
    private jsonDatabase database;

    private WardsManager() {
        wards = new ConcurrentHashMap<>();
        types =  new HashSet<>();
    }

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

        Schedulers.async().run(() -> {

            long startTime = System.nanoTime();
            plugin.getLogger().info("Loading database...");

            database.deserialize().forEach(ward -> {
                if (ward == null) return;

                wards.put(ward.getCenter(), ward);
                utils.setWardsMetadata(ward.getCenter(), ward.getOwner());
            });

            long elapsedTime = System.nanoTime() - startTime;
            plugin.getLogger().info("Loaded " + wards.size() + " elements correctly in "
                    + elapsedTime/1000000 + " ms");

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

    public Set<Ward> getWards(Player p) {
        return wards.values().stream()
                .filter(ward -> ward.getOwner().equals(p.getUniqueId()))
                .collect(Collectors.toSet());
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
        if (!wards.isEmpty()) database.serialize(wards.values());
        plugin.getLogger().info("Database saved correctly");
    }

    public void reload() {
        WardsCooldownTask.unload();
        WardsWatchTask.unload();
        WardsUpdateTask.unload();
        WardsShowTask.unloadAll();

        saveWards();

        types.forEach(WardType::destroy);           // Destroy recipes
        types.clear();
        wards.forEach((location, ward) -> ward.destroy());
        wards.clear();

        init();
    }

    public void destroy() {
        WardsCooldownTask.unload();
        WardsWatchTask.unload();
        WardsUpdateTask.unload();

        types.forEach(WardType::destroy);

        saveWards();
    }

}
