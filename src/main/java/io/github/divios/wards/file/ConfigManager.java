package io.github.divios.wards.file;

import io.github.divios.wards.Wards;

import java.io.File;
import java.util.stream.Stream;

public class ConfigManager {

    private static final Wards plugin = Wards.getInstance();

    private static ConfigManager instance = null;
    private static final int version = 103;

    private final settingsYml configValues;
    private final langYml langValues;
    private final guiYml guiValues;


    private ConfigManager() {
        configValues = new settingsYml();
        langValues = new langYml();
        guiValues = new guiYml();
    }

    /**
     * Checks if all required files exist and if not, creates it with default values
     */
    public static ConfigManager load() {

        if (instance != null) return instance;

        instance = new ConfigManager();

        File wardsDirectory = new File(plugin.getDataFolder(), "wards");

        if (!wardsDirectory.exists()) {
            wardsDirectory.mkdir();
            Stream.of("Sentinel", "Guardian").forEach(s ->
                    plugin.saveResource("wards/" + s + ".yml", false));
        }

        return instance;

    }

    public void reload() {
        configValues.reload();
        langValues.reload();
        guiValues.reload();
    }

    public settingsYml getConfigValues() {
        return configValues;
    }

    public langYml getLangValues() {
        return langValues;
    }

    public guiYml getGuiValues() {
        return guiValues;
    }
}
