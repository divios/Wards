package io.github.divios.wards;

import io.github.divios.core_lib.Core_lib;
import io.github.divios.core_lib.commands.CommandManager;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.utils.Log;
import io.github.divios.wards.commands.giveCmd;
import io.github.divios.wards.commands.helpCmd;
import io.github.divios.wards.commands.listCmd;
import io.github.divios.wards.commands.reloadCmd;
import io.github.divios.wards.file.ConfigManager;
import io.github.divios.wards.observer.ObservablesManager;
import io.github.divios.wards.wards.WardsManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wards extends JavaPlugin {

    private static Wards INSTANCE = null;

    public static final String WARD_META = "Ward_meta";
    public static final String WARD_BLOCK = "Ward_Block";
    public static final String WARD_NAME = "Ward_name";
    public static final String WARD_OWNER = "Ward_uuid";
    public static final String WARD_ACCEPTED = "Ward_accepted";
    public static final String WARD_ID = "Ward_id";
    public static final String WARD_TIMER = "Ward_timer";

    public static ConfigManager configManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Core_lib.setPlugin(this);

        new Metrics(INSTANCE, 12133);       // initiate bstats

        if (Core_lib.MID_VERSION < 12) {
            Log.severe("Unsupported version, disabling...");
            Bukkit.getPluginManager().disablePlugin(INSTANCE);
            return;
        }

        configManager = ConfigManager.load();

        WardsManager.getInstance();
        ObservablesManager.getInstance();  // Loads all Listeners

        CommandManager.register(INSTANCE.getCommand("Wards"));      // Load command Manager
        CommandManager.addCommand(new giveCmd(), new reloadCmd(), new helpCmd(), new listCmd());

        Msg.setPREFIX(FormatUtils.color("&9&lWards &7> "));
        Msg.setTeleportCancelled(configManager.getLangValues().WARD_TELEPORT_CANCELLED);
    }

    @Override
    public void onDisable() {
        WardsManager.getInstance().destroy();         // saves all wards to .json
    }

    public static Wards getInstance() { return INSTANCE; }

    public static void reload() {
        INSTANCE.reloadConfig();

        configManager.reload();
        WardsManager.getInstance().reload();

        Msg.setTeleportCancelled(configManager.getLangValues().WARD_TELEPORT_CANCELLED);
    }

}
