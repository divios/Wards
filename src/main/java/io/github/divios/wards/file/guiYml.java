package io.github.divios.wards.file;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Splitter;
import io.github.divios.core_lib.misc.FormatUtils;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class guiYml extends resource {

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
    public String ROLES_NAME;
    public List<String> ROLES_LORE;
    public XMaterial ROLES_MATERIAL;
    public String MUTE_NAME;
    public List<String> MUTE_LORE;
    public XMaterial MUTE_MATERIAL;
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
    public String ROLES_GUI_TITLE;
    public String ROLES_GUI_RETURN;
    public String ROLES_GUI_ADD_NAME;
    public List<String> ROLES_GUI_ADD_LORE;
    public String ROLES_GUI_PROMPT_TITLE;
    public String ROLES_GUI_PROMPT_SUBTITLE;

    public guiYml() {
        super("gui.yml");
    }

    protected void init() {

        GLASS_PANE_1 = XMaterial.matchXMaterial(yaml
                .getString("gui.glass.glass_pane_1")).orElse(XMaterial.BLUE_STAINED_GLASS_PANE);
        GLASS_PANE_2 = XMaterial.matchXMaterial(yaml
                .getString("gui.glass.glass_pane_2")).orElse(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE);
        GLASS_PANE_3 = XMaterial.matchXMaterial(yaml
                .getString("gui.glass.glass_pane_3")).orElse(XMaterial.GRAY_STAINED_GLASS_PANE);

        TIME_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.time.material")).orElse(XMaterial.CLOCK);
        TIME_NAME = yaml.getString("gui.time.name");
        TIME_LORE = Arrays.asList(yaml.getString("gui.time.lore").split("\\|"));

        TYPE_LORE = Arrays.asList(yaml.getString("gui.type.lore").split("\\|"));

        SHOW_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.show.material")).orElse(XMaterial.NETHER_STAR);
        SHOW_NAME = yaml.getString("gui.show.name");
        SHOW_LORE = Arrays.asList(yaml.getString("gui.show.lore").split("\\|"));

        SETTINGS_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.settings.material")).orElse(XMaterial.PAPER);
        SETTINGS_NAME = yaml.getString("gui.settings.name");
        SETTINGS_LORE = Arrays.asList(yaml.getString("gui.settings.lore").split("\\|"));

        CHANGE_NAME_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.change_name.material")).orElse(XMaterial.PAPER);
        CHANGE_NAME_NAME = yaml.getString("gui.change_name.name");
        CHANGE_NAME_LORE = Arrays.asList(yaml.getString("gui.change_name.lore").split("\\|"));
        CHANGE_NAME_TITLE = yaml.getString("gui.change_name.title");
        CHANGE_NAME_SUBTITLE = yaml.getString("gui.change_name.subtitle");
        CHANGE_NAME_NOT_EMPTY = yaml.getString("gui.change_name.not_empty");

        ROLES_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.change_name.material")).orElse(XMaterial.PAPER);
        ROLES_NAME = yaml.getString("gui.roles.name");
        ROLES_LORE = Arrays.asList(yaml.getString("gui.roles.lore").split("\\|"));

        MUTE_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.change_name.material")).orElse(XMaterial.PAPER);
        MUTE_NAME = yaml.getString("gui.roles.name");
        MUTE_LORE = Arrays.asList(yaml.getString("gui.roles.lore").split("\\|"));

        RETURN_MATERIAL = XMaterial.matchXMaterial(yaml.getString("gui.return.material")).orElse(XMaterial.OAK_DOOR);
        RETURN_NAME = yaml.getString("gui.return.name");
        RETURN_LORE = Arrays.asList(yaml.getString("gui.return.lore").split("\\|"));

        CONFIRM_TITLE = FormatUtils.color(yaml.getString("gui.confirm.title"));
        CONFIRM_YES = FormatUtils.color(yaml.getString("gui.confirm.yes_name"));
        CONFIRM_NO = FormatUtils.color(yaml.getString("gui.confirm.no_name"));

        LIST_TITLE = yaml.getString("gui.list_gui.title");
        LIST_LORE = Splitter.on("|").splitToList(yaml.getString("gui.list_gui.lore"));
        LIST_NOT_SAFE = yaml.getString("gui.list_gui.not_safe");
        LIST_NEXT = yaml.getString("gui.list_gui.next");
        LIST_PREVIOUS = yaml.getString("gui.list_gui.previous");

        ROLES_GUI_TITLE = yaml.getString("gui.roles_gui.title");
        ROLES_GUI_RETURN = yaml.getString("gui.roles_gui.return");
        ROLES_GUI_ADD_NAME = yaml.getString("gui.roles_gui.add_name");
        ROLES_GUI_ADD_LORE = Splitter.on("|").splitToList(yaml.getString("gui.roles_gui.add_lore"));
        ROLES_GUI_PROMPT_TITLE = yaml.getString("gui.roles_gui.prompt_title");
        ROLES_GUI_PROMPT_SUBTITLE = yaml.getString("gui.roles_gui.prompt_subtitle");
    }

    protected void setDefaults() {
    }


}
