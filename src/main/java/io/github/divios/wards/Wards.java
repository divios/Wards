package io.github.divios.wards;

import io.github.divios.wards.file.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wards extends JavaPlugin {

    private static Wards INSTANCE = null;

    @Override
    public void onEnable() {
        INSTANCE = this;

        ConfigManager.load();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Wards getInstance() { return INSTANCE; }
}
