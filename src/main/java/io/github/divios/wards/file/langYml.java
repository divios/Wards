package io.github.divios.wards.file;

import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.wards.Wards;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class langYml {

    private static final Wards plugin = Wards.getInstance();

    private static final String DEFAULT_WARD_ENTERED = FormatUtils.color("{player} &7entered your ward {ward}");
    private static final String DEFAULT_WARD_EXITED = FormatUtils.color("{player} &7exited your ward {ward}");
    private static final String DEFAULT_WARD_EXPIRED = FormatUtils.color("Your ward {ward} has expired");
    private static final String DEFAULT_WARD_PICK_UP = FormatUtils.color("You took your guard {ward}");
    private static final String DEFAULT_WARD_COOLDOWN = FormatUtils.color("&7A cooldown is active, wait a few seconds");
    
    private static final String DEFAULT_GIVE_ON_CMD = FormatUtils.color("You received a {type} &7ward");
    private static final String DEFAULT_GIVE_INFO = FormatUtils.color("&8- &9/wards give [type] [player] &8 - &7Gives the a ward of the selected type for yourself or the given player");
    
    private static final String DEFAULT_LIST_INFO = FormatUtils.color("&8- &9/wards list [player] &8 - &7Prints all the wards of yourself or given player");

    private static final String DEFAULT_HELP_INFO = FormatUtils.color("&8- &9/wards help &8 - &7Shows the plugin's help");
    
    private static final String DEFAULT_RELOAD_INFO = FormatUtils.color("&8- &9/wards reload &8 - &7Reloads the plugin");

    public String WARD_ENTERED;
    public String WARD_EXITED;
    public String WARD_EXPIRED;
    public String WARD_PICK_UP;
    public String WARD_COOLDOWN;

    public String GIVE_ON_CMD;
    public String GIVE_INFO;

    public String LIST_INFO;

    public String HELP_INFO;

    public String RELOAD_INFO;
    
    
    public langYml() {
        init();
    }

    private void init() {
        File file = new File(plugin.getDataFolder() + File.separator + "lang.yml");

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

        WARD_ENTERED = FormatUtils.color(yaml.getString("message.ward.entered", DEFAULT_WARD_ENTERED));
        WARD_EXITED = FormatUtils.color(yaml.getString("message.ward.exited", DEFAULT_WARD_EXITED));
        WARD_EXPIRED = FormatUtils.color(yaml.getString("message.ward.expired", DEFAULT_WARD_EXPIRED));
        WARD_PICK_UP = FormatUtils.color(yaml.getString("message.ward.pick_up", DEFAULT_WARD_PICK_UP));
        WARD_COOLDOWN = FormatUtils.color(yaml.getString("message.ward.cooldown", DEFAULT_WARD_COOLDOWN));

        GIVE_ON_CMD = FormatUtils.color(yaml.getString("message.commands.give.onCmd", DEFAULT_GIVE_ON_CMD));
        GIVE_INFO = FormatUtils.color(yaml.getString("message.commands.give.info", DEFAULT_GIVE_INFO));

        LIST_INFO = FormatUtils.color(yaml.getString("message.commands.list.info", DEFAULT_LIST_INFO));

        HELP_INFO = FormatUtils.color(yaml.getString("message.commands.help.info", DEFAULT_HELP_INFO));

        RELOAD_INFO = FormatUtils.color(yaml.getString("message.commands.reload.info", DEFAULT_RELOAD_INFO));

    }

    private void setDefaults() {

        WARD_ENTERED = DEFAULT_WARD_ENTERED;
        WARD_EXITED = DEFAULT_WARD_EXITED;
        WARD_EXPIRED = DEFAULT_WARD_EXPIRED;
        WARD_COOLDOWN = DEFAULT_WARD_COOLDOWN;

        GIVE_ON_CMD = DEFAULT_GIVE_ON_CMD;
        GIVE_INFO = DEFAULT_GIVE_INFO;

        LIST_INFO = DEFAULT_LIST_INFO;

        HELP_INFO = DEFAULT_HELP_INFO;

        RELOAD_INFO = DEFAULT_RELOAD_INFO;
    }

}
