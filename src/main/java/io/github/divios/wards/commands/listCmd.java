package io.github.divios.wards.commands;

import io.github.divios.core_lib.commands.abstractCommand;
import io.github.divios.core_lib.commands.cmdTypes;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.menus.wardListMenu;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class listCmd extends abstractCommand {

    public listCmd() {
        super(cmdTypes.PLAYERS);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public boolean validArgs(List<String> args) {
        if (args.size() == 0) return true;

        return Bukkit.getPlayer(args.get(0)) != null;       // if player is online

    }

    @Override
    public String getHelp() {
        return Wards.configManager.getLangValues().LIST_INFO;
    }

    @Override
    public List<String> getPerms() {
        return Collections.singletonList("wards.list");
    }

    @Override
    public List<String> getTabCompletition(List<String> args) {
        return Bukkit.getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void run(CommandSender sender, List<String> args) {

        if (args.size() == 1 && !sender.hasPermission("wards.list.other")) return;

        Player p = args.size() == 1 ? Bukkit.getPlayer(args.get(0)) : (Player) sender;

        List<Ward> wards = WardsManager.getInstance().getWards().values().stream()
                .filter(ward -> ward.getOwner().equals(p.getUniqueId()))
                .collect(Collectors.toList());

        if (wards.isEmpty()) {
            Msg.sendMsg(p, "&7You dont have any wards");
            return;
        }

        wardListMenu.prompt(p, wards);
    }
}
