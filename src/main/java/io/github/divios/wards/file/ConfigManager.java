package io.github.divios.wards.file;

import io.github.divios.wards.Wards;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static io.github.divios.core_lib.config.configUtils.copyContents;

public class ConfigManager {

    private static final Wards plugin = Wards.getInstance();

    /**
     * Checks if all required files exist and if not, creates it with default values
     */
    public static void load() {

        plugin.saveDefaultConfig();
        File localeDirectory = new File(plugin.getDataFolder() + File.separator + "locales");
        File wardsDirectory = new File(plugin.getDataFolder() + File.separator + "wards");

        if (!localeDirectory.exists()) localeDirectory.mkdir();  // TODO create languages

        if (!wardsDirectory.exists()) {
            wardsDirectory.mkdir();
            Stream.of("Sentinel", "Guardian").forEach(s -> {
                InputStream input = plugin.getResource("wards/" + s + ".yml");
                File wardFile = new File(plugin.getDataFolder() +
                        File.separator + "wards" + File.separator + s + ".yml");

                if (wardFile.exists()) return;
                createFile(wardFile);

                try {
                    copyContents(input, wardFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private static void createFile(File f) {
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
