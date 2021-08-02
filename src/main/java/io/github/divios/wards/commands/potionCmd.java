package io.github.divios.wards.commands;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.commands.abstractCommand;
import io.github.divios.core_lib.commands.cmdTypes;
import io.github.divios.core_lib.inventory.inventoryUtils;
import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.potions.potionFactory;
import io.github.divios.wards.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class potionCmd extends abstractCommand {

    public potionCmd() {
        super(cmdTypes.BOTH);
    }

    @Override
    public String getName() {
        return "potion";
    }

    @Override
    public boolean validArgs(List<String> args) {

        if (args.size() < 2) return false;

        if (!utils.isInteger(args.get(0)) &&
                Integer.parseInt(args.get(0)) <= 0) return false;

        if (!Arrays.asList("POTION", "SPLASH_POTION", "LINGERING_POTION")
                .contains(args.get(1).toUpperCase())) return false;

        if (args.size() == 3 && Bukkit.getPlayer(args.get(2)) == null) return false;

        return utils.isInteger(args.get(3));

    }

    @Override
    public String getHelp() {
        return Wards.configManager.getLangValues().POTION_INFO;
    }

    @Override
    public List<String> getPerms() {
        return Collections.singletonList("wards.givePotion");
    }

    @Override
    public List<String> getTabCompletition(List<String> args) {

        if (args.size() == 1) return Arrays.asList("300", "500", "1000", "10000");

        else if (args.size() == 2) return Arrays.asList("SPLASH_POTION", "POTION", "LINGERING_POTION");

        else if (args.size() == 3) return Bukkit.getOnlinePlayers().stream()
                .map(HumanEntity::getName).collect(Collectors.toList());

        else if (args.size() == 4) return Arrays.asList("1", "5", "10", "50");

        return Collections.emptyList();
    }

    @Override
    public void run(CommandSender sender, List<String> args) {

        Player p = args.size() >= 3 ? Bukkit.getPlayer(args.get(2)) : (Player) sender;
        Material type = XMaterial.matchXMaterial(args.get(1)).orElse(XMaterial.POTION).parseMaterial();
        int duration = Integer.parseInt(args.get(0));
        int amount = args.size() == 4 ? Integer.parseInt(args.get(3)) : 1;

        if (inventoryUtils.playerEmptySlots(p) < amount) {
            Msg.sendMsg(p, Wards.configManager.getLangValues().WARD_NO_SPACE);
            return;
        }

        ItemUtils.give(p, potionFactory.createPotion(type, duration), amount);
        Msg.sendMsg(p,
                Msg.singletonMsg(Wards.configManager.getLangValues().POTION_ON_CMD)
                        .add("\\{duration}", String.valueOf(duration))
                        .add("\\{amount}", String.valueOf(amount))
                        .build()
        );

    }
}
