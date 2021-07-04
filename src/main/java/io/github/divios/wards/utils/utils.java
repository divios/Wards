package io.github.divios.wards.utils;

import com.cryptomorin.xseries.XSound;
import io.github.divios.wards.Wards;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.*;
import java.util.UUID;

public class utils {

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

}
