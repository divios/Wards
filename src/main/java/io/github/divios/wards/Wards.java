package io.github.divios.wards;

import io.github.divios.core_lib.commands.CommandManager;
import io.github.divios.wards.commands.giveCmd;
import io.github.divios.wards.file.ConfigManager;
import io.github.divios.wards.observer.ObservablesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wards extends JavaPlugin {

    private static Wards INSTANCE = null;

    public static final String WARD_META = "Ward_meta";
    public static final String WARD_BLOCK = "Ward_Block";

    @Override
    public void onEnable() {
        INSTANCE = this;

        ConfigManager.load();
        ObservablesManager.getInstance();  // Loads all Listeners

        CommandManager.register(INSTANCE.getCommand("Wards"));  // Load command Manager
        CommandManager.addCommand(new giveCmd());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Wards getInstance() { return INSTANCE; }
}
