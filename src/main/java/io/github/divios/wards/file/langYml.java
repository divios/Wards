package io.github.divios.wards.file;

import io.github.divios.core_lib.misc.FormatUtils;

public class langYml extends resource {

    public String WARD_ENTERED;
    public String WARD_EXITED;
    public String WARD_EXPIRED;
    public String WARD_PICK_UP;
    public String WARD_LIMIT;
    public String WARD_PICK_DENY;
    public String WARD_COOLDOWN;
    public String WARD_NO_PERMS;
    public String WARD_PLAYER_NOTEXITS;
    public String WARD_OWNER_DENY_REMOVE;
    public String WARD_TELEPORT_COOLDOWN;
    public String WARD_TELEPORT_CANCELLED;

    public String GIVE_ON_CMD;
    public String GIVE_INFO;

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
        WARD_PLAYER_NOTEXITS = FormatUtils.color(yaml.getString("messages.ward.player_not_exits"));
        WARD_OWNER_DENY_REMOVE = FormatUtils.color(yaml.getString("messages.ward.owner_deny_remove"));
        WARD_NO_PERMS = FormatUtils.color(yaml.getString("messages.ward.no_perms"));
        WARD_TELEPORT_COOLDOWN = FormatUtils.color(yaml.getString("messages.ward.teleport_cooldown"));
        WARD_TELEPORT_CANCELLED = FormatUtils.color(yaml.getString("messages.ward.teleport_cancelled"));

        GIVE_ON_CMD = FormatUtils.color(yaml.getString("messages.commands.give.onCmd"));
        GIVE_INFO = FormatUtils.color(yaml.getString("messages.commands.give.info"));

        LIST_INFO = FormatUtils.color(yaml.getString("messages.commands.list.info"));

        HELP_INFO = FormatUtils.color(yaml.getString("messages.commands.help.info"));

        RELOAD_INFO = FormatUtils.color(yaml.getString("messages.commands.reload.info"));

    }

    protected void setDefaults() {
    }

}
