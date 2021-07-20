package io.github.divios.wards.file;

import com.google.common.collect.Lists;
import io.github.divios.wards.Wards;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;

public abstract class resource {

    private static final Wards plugin = Wards.getInstance();

    private final String name;

    private File file;
    protected YamlConfiguration yaml;

    protected resource(String name) {
        this.name = name;
        create();
    }

    public void create() {

       file = new File(plugin.getDataFolder(), name);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(name, false);
        }

        yaml = YamlConfiguration.loadConfiguration(file);
        copyDefaults();

        init();
    }

    protected abstract void init();

    @Deprecated
    protected abstract void setDefaults();

    protected List<String> getSetLines() {
        return Lists.newArrayList(yaml.getKeys(true));
    }

    public void reload() {
        create();
    }

    private void copyDefaults() {
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(plugin.getResource(name), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        YamlConfiguration defConfig = null;
        if (defConfigStream != null) {
            defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            yaml.setDefaults(defConfig);
            yaml.options().copyDefaults(true);
        }

        try { yaml.save(file); }
        catch (IOException e) { e.printStackTrace(); }

        if (defConfig != null) yaml.setDefaults(defConfig);
    }

}