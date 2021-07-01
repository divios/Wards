package io.github.divios.wards.commands;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.commands.abstractCommand;
import io.github.divios.core_lib.commands.cmdTypes;
import io.github.divios.wards.Wards;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

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
        return true;
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
        return Arrays.asList("give");
    }

    @Override
    public void run(CommandSender sender, List<String> args) {
        NBTItem item = new NBTItem(XMaterial.RESPAWN_ANCHOR.parseItem());
        item.setString(Wards.WARD_UUID, ((Player) sender).getUniqueId().toString());
        item.setString(Wards.WARD_ID, "oke");  // TODO: Set ward type
        item.setBoolean(Wards.WARD_META, true);

        ((Player) sender).getInventory().addItem(item.getItem());
    }
}
