package io.github.divios.wards;

import io.github.divios.core_lib.commands.CommandManager;
import io.github.divios.wards.commands.giveCmd;
import io.github.divios.wards.file.ConfigManager;
import io.github.divios.wards.observer.ObservablesManager;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wards extends JavaPlugin {

    private static Wards INSTANCE = null;

    public static final String WARD_META = "Ward_meta";
    public static final String WARD_BLOCK = "Ward_Block";
    public static final String WARD_OWNER = "Ward_uuid";
    public static final String WARD_ID = "Ward_id";
    public static final String WARD_TIMER= "Ward_timer";

    @Override
    public void onEnable() {
        INSTANCE = this;

        ConfigManager.load();
        ObservablesManager.getInstance();  // Loads all Listeners
        WardsManager.getInstance();

        CommandManager.register(INSTANCE.getCommand("Wards"));  // Load command Manager
        CommandManager.addCommand(new giveCmd());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Wards getInstance() { return INSTANCE; }
}
