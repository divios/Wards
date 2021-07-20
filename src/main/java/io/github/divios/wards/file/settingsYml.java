package io.github.divios.wards.file;

public class settingsYml extends resource {

    public Integer CHUNK_DISPLAY_SECONDS;
    public Integer CHUNK_DISPLAY_COOLDOWN;
    public Integer WARD_CHECK_SECONDS;
    public Integer WARD_TELEPORT_DELAY;

    public settingsYml() {
        super("settings.yml");
    }

    protected void init() {

        CHUNK_DISPLAY_SECONDS = yaml.getInt("chunk_display_seconds");
        CHUNK_DISPLAY_COOLDOWN = yaml.getInt("chunk_display_cooldown");
        WARD_CHECK_SECONDS = yaml.getInt("ward_check_cycle_seconds");
        WARD_TELEPORT_DELAY = yaml.getInt("ward_teleport_delay");

    }

    protected void setDefaults() {

    }

}
