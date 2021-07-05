package io.github.divios.wards.commands;

import io.github.divios.core_lib.commands.abstractCommand;
import io.github.divios.core_lib.commands.cmdTypes;
import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.WardType;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        if (args.size() == 3) {
            return WardsManager.getInstance().getWardsTypes().stream()
                    .anyMatch(wardType -> wardType.getId().equals(args.get(0))) &&
                    Bukkit.getPlayer(args.get(1)) != null &&
                    !utils.isInteger(args.get(2));
        }

        return false;

    }

    @Override
    public String getHelp() {
        return Wards.langValues.GIVE_INFO;
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

        if (args.size() == 3) {
            List<String> toReturn = new ArrayList<>();
            IntStream.range(1, 10).forEach(value -> toReturn.add("" + value));
            return toReturn;
        }

        return Collections.emptyList();
    }

    @Override
    public void run(CommandSender sender, List<String> args) {

        Player p = args.size() == 2 ? Bukkit.getPlayer(args.get(1)): (Player) sender;
        int amount = args.size() >= 3 ? Integer.parseInt(args.get(2)):1;

        if (args.size() >= 2 && !p.hasPermission("wards.give.others")) return;

        WardsManager.getInstance().getWardsTypes().stream()
                .filter(wardType -> wardType.getId().equals(args.get(0)))
                .findFirst()
                .ifPresent(wardType -> {
                    Msg.sendMsg(p, Msg.singletonMsg(Wards.langValues.GIVE_ON_CMD)
                            .add("\\{type}", wardType.getId()).build());
                    ItemUtils.give(p, wardType.buildItem(p), amount);
                });


    }
}
