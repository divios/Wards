package io.github.divios.wards.potions;

import org.bukkit.entity.Player;

public class potionManager {

    private static potionManager INSTANCE = null;
    private final cooldownMap cooldownMaps;

    private potionManager() {
        cooldownMaps = cooldownMap.create();
    }

    public static potionManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new potionManager();

        return INSTANCE;
    }


    public void add(Player p, int duration) {
        cooldownMaps.add(p, duration);
    }

    public boolean contains(Player p) {
        return cooldownMaps.exist(p);
    }


}
