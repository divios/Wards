package io.github.divios.wards.wards;

import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.utils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WardsParser {

    private static final Wards plugin = Wards.getInstance();

    protected static Set<WardType> parse() {

        Set<WardType> types = new HashSet<>();

        File wardsDir = new File(plugin.getDataFolder() + File.separator + "wards");

        Arrays.stream(wardsDir.listFiles()).forEach(file -> {

            plugin.getLogger().info("Found " + file.getName());
            if (utils.isEmpty(file)) {
                plugin.getLogger().info(file.getName() + " is empty, skipping");
                return;
            }

            YamlConfiguration yaml = new YamlConfiguration();
            try {
                yaml.load(file);
            } catch (InvalidConfigurationException|IOException e) {
                plugin.getLogger().severe("File " + file.getName() + " couldn't be loaded, check the format," +
                        " it must be .yml");
            }

            WardType type;
            try {

                type = new WardType.Builder(yaml.getString("id"))
                        .setMaterial(yaml.getString("material"))
                        .setDisplay_name(yaml.getString("display_name"))
                        .setLore(yaml.getString("lore"))
                        .setTime(yaml.contains("time") ? yaml.getInt("time"):null)
                        .setType(yaml.getString("type"))
                        .setRadius(yaml.contains("radius") ? yaml.getInt("radius"):null)
                        .build();

            } catch (WardType.WardsTypeException e) {
                plugin.getLogger().severe("There was a problem parsing the file " + file.getName()
                    + " , found the error on the field: " + e.getWho());
                return;
            }

            plugin.getLogger().info("Registered " + type.getId() + " correctly");
            types.add(type);

        });

        return types;
    }

}
