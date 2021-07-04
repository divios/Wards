package io.github.divios.wards.file;

import io.github.divios.wards.Wards;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class configYml {

    private static final Wards plugin = Wards.getInstance();

    public static Integer DEFAULT_CHUNK_DISPLAY_SECONDS = 10;
    public static Integer DEFAULT_CHUNK_DISPLAY_COOLDOWN = 30;
    public static Integer DEFAULT_WARD_CHECK_SECONDS = 20;

    public Integer CHUNK_DISPLAY_SECONDS;
    public Integer CHUNK_DISPLAY_COOLDOWN;
    public Integer WARD_CHECK_SECONDS;

    public configYml() {
        init();
    }

    private void init() {
        File file = new File(plugin.getDataFolder() +
                File.separator + "config.yml");

        if (!file.exists()) {
            setDefaults();
            return;
        }

        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            setDefaults();
            return;
        }

        CHUNK_DISPLAY_SECONDS = yaml.getInt("chunk_display_seconds", DEFAULT_CHUNK_DISPLAY_SECONDS);
        CHUNK_DISPLAY_COOLDOWN = yaml.getInt("chunk_display_cooldown", DEFAULT_CHUNK_DISPLAY_COOLDOWN);
        WARD_CHECK_SECONDS = yaml.getInt("ward_check_cycle_seconds", DEFAULT_WARD_CHECK_SECONDS);

    }

    private void setDefaults() {
        CHUNK_DISPLAY_SECONDS = DEFAULT_CHUNK_DISPLAY_SECONDS;
        CHUNK_DISPLAY_COOLDOWN = DEFAULT_CHUNK_DISPLAY_COOLDOWN;
        WARD_CHECK_SECONDS = DEFAULT_WARD_CHECK_SECONDS;
    }

}
