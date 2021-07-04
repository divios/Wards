package io.github.divios.wards.commands;

import com.cryptomorin.xseries.messages.Titles;
import io.github.divios.core_lib.commands.abstractCommand;
import io.github.divios.core_lib.commands.cmdTypes;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.wards.Wards;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class reloadCmd extends abstractCommand {

    public reloadCmd() {
        super(cmdTypes.BOTH);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public boolean validArgs(List<String> args) {
        return true;
    }

    @Override
    public String getHelp() {
        return Wards.langValues.RELOAD_INFO;
    }

    @Override
    public List<String> getPerms() {
        return Collections.singletonList("wards.reload");
    }

    @Override
    public List<String> getTabCompletition(List<String> args) {
        return Collections.emptyList();
    }

    @Override
    public void run(CommandSender sender, List<String> args) {
        Wards.reload();
        if (sender instanceof Player)
            Titles.sendTitle((Player) sender, 10, 20, 10,
                FormatUtils.color("&9&lWards"), FormatUtils.color("&8Successful reload"));
    }
}
