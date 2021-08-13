package io.github.divios.wards.file;

import com.google.common.base.Splitter;
import io.github.divios.core_lib.misc.FormatUtils;

import java.util.List;

public class langYml extends resource {

    public String WARD_ENTERED;
    public String WARD_EXITED;
    public String WARD_EXPIRED;
    public String WARD_PICK_UP;
    public String WARD_LIMIT;
    public String WARD_PICK_DENY;
    public String WARD_COOLDOWN;

    public String WARD_NO_PERMS;
    public String WARD_NO_SPACE;
    public String WARD_PLAYER_NO_EXIST;
    public String WARD_OWNER_DENY_REMOVE;
    public String WARD_TELEPORT_COOLDOWN;
    public String WARD_TELEPORT_CANCELLED;
    public String WARD_PING;
    public String WARD_UNPING;

    public String GIVE_ON_CMD;
    public String GIVE_INFO;

    public String POTION_ON_CMD;
    public String POTION_INFO;
    public String POTION_NAME;
    public List<String> POTION_LORE;
    public String POTION_DRINK;
    public String POTION_EXPIRE;

    public String LIST_INFO;

    public String HELP_INFO;

    public String RELOAD_INFO;
    
    
    public langYml() {
        super("lang.yml");
    }

    protected void init() {

        WARD_ENTERED = FormatUtils.color(yaml.getString("messages.ward.entered"));
        WARD_EXITED = FormatUtils.color(yaml.getString("messages.ward.exited"));
        WARD_EXPIRED = FormatUtils.color(yaml.getString("messages.ward.expired"));
        WARD_PICK_UP = FormatUtils.color(yaml.getString("messages.ward.pick_up"));
        WARD_LIMIT = FormatUtils.color(yaml.getString("messages.ward.limit"));
        WARD_PICK_DENY = FormatUtils.color(yaml.getString("messages.ward.pick_deny"));
        WARD_COOLDOWN = FormatUtils.color(yaml.getString("messages.ward.cooldown"));
        WARD_PLAYER_NO_EXIST = FormatUtils.color(yaml.getString("messages.ward.player_not_exits"));
        WARD_OWNER_DENY_REMOVE = FormatUtils.color(yaml.getString("messages.ward.owner_deny_remove"));
        WARD_NO_PERMS = FormatUtils.color(yaml.getString("messages.ward.no_perms"));
        WARD_NO_SPACE = FormatUtils.color(yaml.getString("messages.ward.no_space"));
        WARD_TELEPORT_COOLDOWN = FormatUtils.color(yaml.getString("messages.ward.teleport_cooldown"));
        WARD_TELEPORT_CANCELLED = FormatUtils.color(yaml.getString("messages.ward.teleport_cancelled"));
        WARD_PING = FormatUtils.color(yaml.getString("messages.ward.ping"));
        WARD_UNPING = FormatUtils.color(yaml.getString("messages.ward.unping"));

        GIVE_ON_CMD = FormatUtils.color(yaml.getString("messages.commands.give.onCmd"));
        GIVE_INFO = FormatUtils.color(yaml.getString("messages.commands.give.info"));

        POTION_ON_CMD = FormatUtils.color(yaml.getString("messages.commands.potion.onCmd"));
        POTION_INFO = FormatUtils.color(yaml.getString("messages.commands.potion.info"));
        POTION_NAME = FormatUtils.color(yaml.getString("messages.commands.potion.name"));
        POTION_LORE = Splitter.on("|").splitToList(FormatUtils.color(yaml.getString("messages.commands.potion.lore")));
        POTION_DRINK = FormatUtils.color(yaml.getString("messages.commands.potion.drink"));
        POTION_EXPIRE = FormatUtils.color(yaml.getString("messages.commands.potion.expire"));

        LIST_INFO = FormatUtils.color(yaml.getString("messages.commands.list.info"));

        HELP_INFO = FormatUtils.color(yaml.getString("messages.commands.help.info"));

        RELOAD_INFO = FormatUtils.color(yaml.getString("messages.commands.reload.info"));

    }

    protected void setDefaults() {
    }

}
