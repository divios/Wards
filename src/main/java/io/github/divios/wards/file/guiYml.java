package io.github.divios.wards.file;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.wards.Wards;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class guiYml {

    private static final Wards plugin = Wards.getInstance();

    public static final Material GLASS_PANE_1 = XMaterial.BLUE_STAINED_GLASS_PANE.parseMaterial();
    public static final Material GLASS_PANE_2 = XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.parseMaterial();
    public static final Material GLASS_PANE_3 = XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial();

    public final Material TIME_MATERIAL = XMaterial.CLOCK.parseMaterial();
    public final String TIME_NAME = "&6&lRemaining time";
    public final List<String> TIME_LORE =

    public guiYml() {
        init();
    }

    private void init() {
        File file = new File(plugin.getDataFolder() +
                File.separator + "gui.yml");

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

        CHUNK_DISPLAY_SECONDS = yaml.contains("chunk_display_seconds") ?
                yaml.getInt("chunk_display_seconds"):
                DEFAULT_CHUNK_DISPLAY_SECONDS;
        CHUNK_DISPLAY_COOLDOWN = yaml.contains("chunk_display_cooldown") ?
                yaml.getInt("chunk_display_cooldown"):
                DEFAULT_CHUNK_DISPLAY_COOLDOWN;
        WARD_CHECK_SECONDS = yaml.contains("ward_check_cycle_seconds") ?
                yaml.getInt("ward_check_cycle_seconds"):
                DEFAULT_WARD_CHECK_SECONDS;

    }

    private void setDefaults() {
        CHUNK_DISPLAY_SECONDS = DEFAULT_CHUNK_DISPLAY_SECONDS;
        CHUNK_DISPLAY_COOLDOWN = DEFAULT_CHUNK_DISPLAY_COOLDOWN;
        WARD_CHECK_SECONDS = DEFAULT_WARD_CHECK_SECONDS;
    }


}
