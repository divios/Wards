package io.github.divios.wards.file;

import io.github.divios.wards.Wards;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static io.github.divios.core_lib.config.configUtils.copyContents;
import static io.github.divios.core_lib.config.configUtils.createFile;

public class ConfigManager {

    private static final Wards plugin = Wards.getInstance();

    /**
     * Checks if all required files exist and if not, creates it with default values
     */
    public static void load() {

        plugin.saveDefaultConfig();
        File localeDirectory = new File(plugin.getDataFolder() + File.separator + "locales");
        File databaseFile = new File(plugin.getDataFolder() + File.separator + "data.json");
        File wardsDirectory = new File(plugin.getDataFolder() + File.separator + "wards");
        File guiDirectory= new File(plugin.getDataFolder() + File.separator + "gui.yml");

        if (!localeDirectory.exists()) {
            localeDirectory.mkdir();  // TODO create languages

            Stream.of("US").forEach(s -> {
                InputStream input = plugin.getResource("locales/" + s + ".yml");
                File wardFile = new File(plugin.getDataFolder() +
                        File.separator + "locales" + File.separator + s + ".yml");

                if (!wardFile.exists()) {
                    createFile(wardFile);
                }

                try {
                    copyContents(input, wardFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        if (!databaseFile.exists()) createFile(databaseFile);

        if (!guiDirectory.exists()) {
            createFile(guiDirectory);

            InputStream input = plugin.getResource("gui.yml");
            try {
                copyContents(input, guiDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (!wardsDirectory.exists()) {
            wardsDirectory.mkdir();
            Stream.of("Sentinel", "Guardian").forEach(s -> {
                InputStream input = plugin.getResource("wards/" + s + ".yml");
                File wardFile = new File(plugin.getDataFolder() +
                        File.separator + "wards" + File.separator + s + ".yml");

                if (!wardFile.exists()) {
                    createFile(wardFile);
                }

                try {
                    copyContents(input, wardFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
