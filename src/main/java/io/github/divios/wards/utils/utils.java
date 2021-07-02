package io.github.divios.wards.utils;

import io.github.divios.core_lib.misc.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class utils {

    public static void sendMsg(Player p, String msg) {
        if (p == null) return;

        p.sendMessage(FormatUtils.color(msg));
    }

    public static void sendMsg(UUID uuid, String msg) {
        sendMsg(Bukkit.getPlayer(uuid), msg);
    }

}
