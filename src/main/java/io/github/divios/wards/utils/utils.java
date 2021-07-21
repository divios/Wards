package io.github.divios.wards.utils;

import com.cryptomorin.xseries.XSound;
import io.github.divios.core_lib.misc.Task;
import io.github.divios.wards.Wards;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.*;
import java.util.UUID;

public class utils {

    private static final Wards plugin = Wards.getInstance();

    public static boolean isEmpty(File file) {

        FileReader fr = null;   //reads the file
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            return true;
        }

        BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
        StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters
        String line;

        int lines = 0;

        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
                lines++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lines == 0;
    }

    public static void clearUpFile(File file) {
        if (file.delete()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setWardMetadata(Block block, UUID uuid) {
        block.setMetadata(Wards.WARD_META,
                new FixedMetadataValue(Wards.getInstance(), "pizza"));

        block.setMetadata(Wards.WARD_BLOCK,
                new FixedMetadataValue(Wards.getInstance(), uuid.toString()));
    }

    public static void setWardsMetadata(Location loc, UUID uuid) {
        setWardMetadata(loc.getBlock(), uuid);
    }

    public static void sendSound(Player p, Sound s) {
        if (p == null) return;

        p.playSound(p.getLocation(), s, 1, 1);
    }

    public static void sendSound(UUID uuid, Sound s) {
        sendSound(Bukkit.getPlayer(uuid), s);
    }

    public static void sendSound(Player p, XSound s) {
        sendSound(p, s.parseSound());
    }

    public static void sendSound(UUID uuid, XSound s) {
        sendSound(uuid, s.parseSound());
    }

    public static void cleanBlock(Block block) {
        Schedulers.sync().run(() -> {         // make sure is synchronous
            block.setType(Material.AIR);
            block.removeMetadata(Wards.WARD_BLOCK, plugin);
        });
    }

    public static void cleanBlock(Location l) {
        cleanBlock(l.getBlock());
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static void playSound(Player p, XSound sound) {
        playSound(p, sound.parseSound());
    }

    public static void playSound(Player p, Sound sound) {
        p.playSound(p.getLocation(), sound, 1, 1);
    }

    public static Integer getWardsLimit(Player p) {
        final Integer[] limit = {null};
        p.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(perm -> perm.startsWith("wards.limit."))
                .map(perm -> perm.replace("wards.limit.", ""))
                .filter(utils::isInteger)
                .mapToInt(Integer::parseInt)
                .max().ifPresent(value -> limit[0] = value);

        return limit[0];
    }

    public static void setDefaults(YamlConfiguration yaml, String defYaml) {
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(plugin.getResource(defYaml), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            yaml.setDefaults(defConfig);
        }
    }
}
