package io.github.divios.wards.file;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Splitter;
import io.github.divios.wards.Wards;
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
    private static final String DEFAULT_CHANGE_NAME_TITLE = "&e&lChange name";
    private static final String DEFAULT_CHANGE_NAME_SUBTITLE= "&7Input new name";
    private static final String DEFAULT_CHANGE_NAME_NOT_EMPTY = "&7The name cannot be empty!";

    private static final XMaterial DEFAULT_RETURN_MATERIAL = XMaterial.PAPER;
    private static final String DEFAULT_RETURN_NAME = "&c&lReturn";
    private static final List<String> DEFAULT_RETURN_LORE = Collections.singletonList("&7Click to go back");

    private static final String DEFAULT_CONFIRM_TITLE = "&8Confirm Action";
    private static final String DEFAULT_CONFIRM_YES = "&aYES";
    private static final String DEFAULT_CONFIRM_NO = "&cNO";

    private static final String DEFAULT_LIST_TITLE = "&9Wards list of &f {player}";
    private static final List<String> DEFAULT_LIST_LORE= Arrays.asList("", "&8 - &7Type: &9{type}",
            "&8 - &7Radius: &9{radius}", "&8 - &7Location: &9{loc}", "", "&f- Left Click&9 to view more information", "&f- Right Click&9 to teleport");
    private static final String DEFAULT_LIST_NOT_SAFE = "&7The ward you want to teleport is not safe";
    private static final String DEFAULT_LIST_PREVIOUS = "&1&lPrevious";
    private static final String DEFAULT_LIST_NEXT = "&1&lNext";

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
    public String CHANGE_NAME_TITLE;
    public String CHANGE_NAME_SUBTITLE;
    public String CHANGE_NAME_NOT_EMPTY;
    public XMaterial RETURN_MATERIAL;
    public String RETURN_NAME;
    public List<String> RETURN_LORE;
    public String CONFIRM_TITLE;
    public String CONFIRM_YES;
    public String CONFIRM_NO;
    public String LIST_TITLE;
    public List<String> LIST_LORE;
    public String LIST_NOT_SAFE;
    public String LIST_PREVIOUS;
    public String LIST_NEXT;

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
        CHANGE_NAME_TITLE = yaml.getString("gui.change_name.title", DEFAULT_CHANGE_NAME_TITLE);
        CHANGE_NAME_SUBTITLE = yaml.getString("gui.change_name.subtitle", DEFAULT_CHANGE_NAME_SUBTITLE);
        CHANGE_NAME_NOT_EMPTY = yaml.getString("gui.change_name.not_empty", DEFAULT_CHANGE_NAME_NOT_EMPTY);

        RETURN_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.return.material", "CLOCK")).get();
        RETURN_NAME = yaml.getString("gui.return.name", DEFAULT_RETURN_NAME);
        RETURN_LORE = yaml.contains("gui.return.lore") ?
                Arrays.asList(yaml.getString("gui.return.lore").split("\\|")):
                DEFAULT_RETURN_LORE;

        CONFIRM_TITLE = yaml.getString("gui.confirm.title", DEFAULT_CONFIRM_TITLE);
        CONFIRM_YES = yaml.getString("gui.confirm.yes", DEFAULT_CONFIRM_YES);
        CONFIRM_NO = yaml.getString("gui.confirm.no", DEFAULT_CONFIRM_NO);

        LIST_TITLE = yaml.getString("gui.list_gui.title", DEFAULT_LIST_TITLE);
        LIST_LORE = yaml.contains("gui.list_gui.lore") ?
                Splitter.on("|").splitToList(yaml.getString("gui.list_gui.lore")):
                DEFAULT_LIST_LORE;
        LIST_NOT_SAFE = yaml.getString("gui.list_gui.not_safe", DEFAULT_LIST_NOT_SAFE);
        LIST_NEXT = yaml.getString("gui.list_gui.next", DEFAULT_LIST_NEXT);
        LIST_PREVIOUS = yaml.getString("gui.list_gui.previous", DEFAULT_LIST_PREVIOUS);
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
        CHANGE_NAME_TITLE = DEFAULT_CHANGE_NAME_TITLE;
        CHANGE_NAME_SUBTITLE = DEFAULT_CHANGE_NAME_SUBTITLE;
        CHANGE_NAME_NOT_EMPTY = DEFAULT_CHANGE_NAME_NOT_EMPTY;
        RETURN_MATERIAL = DEFAULT_RETURN_MATERIAL;
        RETURN_NAME = DEFAULT_RETURN_NAME;
        RETURN_LORE = DEFAULT_RETURN_LORE;
        CONFIRM_TITLE = DEFAULT_CONFIRM_TITLE;
        CONFIRM_YES = DEFAULT_CONFIRM_YES;
        CONFIRM_NO = DEFAULT_CONFIRM_NO;
        LIST_TITLE = DEFAULT_LIST_TITLE;
        LIST_LORE = DEFAULT_LIST_LORE;
        LIST_NOT_SAFE = DEFAULT_LIST_NOT_SAFE;
        LIST_NEXT = DEFAULT_LIST_NEXT;
        LIST_PREVIOUS = DEFAULT_LIST_PREVIOUS;
    }


}
