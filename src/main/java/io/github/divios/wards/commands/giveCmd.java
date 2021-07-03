package io.github.divios.wards.commands;

import io.github.divios.core_lib.commands.abstractCommand;
import io.github.divios.core_lib.commands.cmdTypes;
import io.github.divios.core_lib.inventory.inventoryUtils;
import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.wards.WardType;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class giveCmd extends abstractCommand {

    public giveCmd() {
        super(cmdTypes.BOTH);
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public boolean validArgs(List<String> args) {
        if (args.size() == 0) return false;

        if (args.size() == 1 &&
                WardsManager.getInstance().getWardsTypes().stream()
                .anyMatch(wardType -> wardType.getId().equals(args.get(0)))) {
            return true;
        }

        if (args.size() == 2) {
            return WardsManager.getInstance().getWardsTypes().stream()
                    .anyMatch(wardType -> wardType.getId().equals(args.get(0))) &&
                            Bukkit.getPlayer(args.get(1)) != null;
        }

        return false;

    }

    @Override
    public String getHelp() {
        return "/wards give";
    }

    @Override
    public List<String> getPerms() {
        return Arrays.asList("wards.give");
    }

    @Override
    public List<String> getTabCompletition(List<String> args) {
        if (args.size() == 1) {
            return WardsManager.getInstance().getWardsTypes()
                    .stream()
                    .map(WardType::getId)
                    .collect(Collectors.toList());
        }

        if (args.size() == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(HumanEntity::getName)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public void run(CommandSender sender, List<String> args) {

        Player p = args.size() == 2 ? Bukkit.getPlayer(args.get(1)): (Player) sender;

        if (inventoryUtils.getFirstEmpty(p.getInventory()) == -1) {
            Msg.sendMsg(p, "&7You don't have space in your inventory");
            return;
        }

        WardsManager.getInstance().getWardsTypes().stream()
                .filter(wardType -> wardType.getId().equals(args.get(0)))
                .findFirst()
                .ifPresent(wardType -> ItemUtils.give(p, wardType.buildItem(p)));


    }
}
