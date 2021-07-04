package io.github.divios.wards.file;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.wards.Wards;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class guiYml {

    private static final Wards plugin = Wards.getInstance();

    private static final XMaterial DEFAULT_GLASS_PANE_1 = XMaterial.BLUE_STAINED_GLASS_PANE;
    private static final XMaterial DEFAULT_GLASS_PANE_2 = XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE;
    private static final XMaterial DEFAULT_GLASS_PANE_3 = XMaterial.GRAY_STAINED_GLASS_PANE;

    private static final XMaterial DEFAULT_TIME_MATERIAL = XMaterial.CLOCK;
    private static final String DEFAULT_TIME_NAME = "&6&lRemaining time";
    private static final List<String> DEFAULT_TIME_LORE = Collections.singletonList("&7Remaining: &6{time}");

    private static final List<String> DEFAULT_TYPE_LORE = Arrays.asList("&7Type: &6{type}", "&7Radius: &6{radius}");

    private static final XMaterial DEFAULT_SHOW_MATERIAL = XMaterial.NETHER_STAR;
    private static final String DEFAULT_SHOW_NAME = "&6&lShow";
    private static final List<String> DEFAULT_SHOW_LORE = Collections.singletonList("&7Show bounds of this ward");

    private static final XMaterial DEFAULT_SETTINGS_MATERIAL = XMaterial.PAPER;
    private static final String DEFAULT_SETTINGS_NAME = "&6&lSettings";
    private static final List<String> DEFAULT_SETTINGS_LORE = Collections.singletonList("&7Click to manage this ward settings");

    private static final XMaterial DEFAULT_CHANGE_NAME_MATERIAL = XMaterial.PAPER;
    private static final String DEFAULT_CHANGE_NAME_NAME = "&E&lChange Name";
    private static final List<String> DEFAULT_CHANGE_NAME_LORE = Collections.singletonList("&7Click to change the name of this ward");

    private static final XMaterial DEFAULT_RETURN_MATERIAL = XMaterial.PAPER;
    private static final String DEFAULT_RETURN_NAME = "&c&lReturn";
    private static final List<String> DEFAULT_RETURN_LORE = Collections.singletonList("&7Click to go back");

    public XMaterial GLASS_PANE_1;
    public XMaterial GLASS_PANE_2;
    public XMaterial GLASS_PANE_3;
    public XMaterial TIME_MATERIAL;
    public String TIME_NAME;
    public List<String> TIME_LORE;
    public List<String> TYPE_LORE;
    public XMaterial SHOW_MATERIAL;
    public String SHOW_NAME;
    public List<String> SHOW_LORE;
    public XMaterial SETTINGS_MATERIAL;
    public String SETTINGS_NAME;
    public List<String> SETTINGS_LORE;
    public XMaterial CHANGE_NAME_MATERIAL;
    public String CHANGE_NAME_NAME;
    public List<String> CHANGE_NAME_LORE;
    public XMaterial RETURN_MATERIAL;
    public String RETURN_NAME;
    public List<String> RETURN_LORE;

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

        GLASS_PANE_1 = XMaterial.matchXMaterial(yaml
                .getString("gui.glass.glass_pane_1", "BLUE_STAINED_GLASS_PANE")).get();
        GLASS_PANE_2 = XMaterial.matchXMaterial(yaml
                .getString("gui.glass.glass_pane_2", "LIGHT_BLUE_STAINED_GLASS_PANE")).get();
        GLASS_PANE_3 = XMaterial.matchXMaterial(yaml
                .getString("gui.glass.glass_pane_3", "GRAY_STAINED_GLASS_PANE")).get();

        TIME_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.time.material", "CLOCK")).get();
        TIME_NAME = yaml.getString("gui.time.name", DEFAULT_TIME_NAME);
        TIME_LORE = yaml.contains("gui.time.lore") ?
                Arrays.asList(yaml.getString("gui.time.lore").split("\\|")):
                DEFAULT_TIME_LORE;

        TYPE_LORE = yaml.contains("gui.type.lore") ?
                Arrays.asList(yaml.getString("gui.type.lore").split("\\|")):
                DEFAULT_TYPE_LORE;

        SHOW_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.show.material", "NETHER_STAR")).get();
        SHOW_NAME = yaml.getString("gui.show.name", DEFAULT_SHOW_NAME);
        SHOW_LORE = yaml.contains("gui.show.lore") ?
                Arrays.asList(yaml.getString("gui.show.lore").split("\\|")):
                DEFAULT_SHOW_LORE;

        SETTINGS_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.settings.material", "CLOCK")).get();
        SETTINGS_NAME = yaml.getString("gui.settings.name", DEFAULT_SETTINGS_NAME);
        SETTINGS_LORE = yaml.contains("gui.settings.lore") ?
                Arrays.asList(yaml.getString("gui.settings.lore").split("\\|")):
                DEFAULT_SETTINGS_LORE;

        CHANGE_NAME_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.change_name.material", "CLOCK")).get();
        CHANGE_NAME_NAME = yaml.getString("gui.change_name.name", DEFAULT_CHANGE_NAME_NAME);
        CHANGE_NAME_LORE = yaml.contains("gui.change_name.lore") ?
                Arrays.asList(yaml.getString("gui.change_name.lore").split("\\|")):
                DEFAULT_CHANGE_NAME_LORE;

        RETURN_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.return.material", "CLOCK")).get();
        RETURN_NAME = yaml.getString("gui.return.name", DEFAULT_RETURN_NAME);
        RETURN_LORE = yaml.contains("gui.return.lore") ?
                Arrays.asList(yaml.getString("gui.return.lore").split("\\|")):
                DEFAULT_RETURN_LORE;

    }

    private void setDefaults() {
        GLASS_PANE_1 = DEFAULT_GLASS_PANE_1;
        GLASS_PANE_2 = DEFAULT_GLASS_PANE_2;
        GLASS_PANE_3 = DEFAULT_GLASS_PANE_3;
        TIME_MATERIAL = DEFAULT_TIME_MATERIAL;
        TIME_NAME = DEFAULT_TIME_NAME;
        TIME_LORE = DEFAULT_TIME_LORE;
        TYPE_LORE = DEFAULT_TYPE_LORE;
        SHOW_MATERIAL = DEFAULT_SHOW_MATERIAL;
        SHOW_NAME = DEFAULT_SHOW_NAME;
        SHOW_LORE = DEFAULT_SHOW_LORE;
        SETTINGS_MATERIAL = DEFAULT_SETTINGS_MATERIAL;
        SETTINGS_NAME = DEFAULT_SETTINGS_NAME;
        SETTINGS_LORE = DEFAULT_SETTINGS_LORE;
        CHANGE_NAME_MATERIAL = DEFAULT_CHANGE_NAME_MATERIAL;
        CHANGE_NAME_NAME = DEFAULT_CHANGE_NAME_NAME;
        CHANGE_NAME_LORE = DEFAULT_CHANGE_NAME_LORE;
        RETURN_MATERIAL = DEFAULT_RETURN_MATERIAL;
        RETURN_NAME = DEFAULT_RETURN_NAME;
        RETURN_LORE = DEFAULT_RETURN_LORE;
    }


}
