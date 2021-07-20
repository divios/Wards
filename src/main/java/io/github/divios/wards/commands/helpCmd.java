package io.github.divios.wards.commands;

import io.github.divios.core_lib.commands.CommandManager;
import io.github.divios.core_lib.commands.abstractCommand;
import io.github.divios.core_lib.commands.cmdTypes;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.wards.Wards;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class helpCmd extends abstractCommand {

    public helpCmd() {
        super(cmdTypes.PLAYERS);
        CommandManager.setDefault(this);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public boolean validArgs(List<String> args) {
        return true;
    }

    @Override
    public String getHelp() {
        return Wards.configManager.getLangValues().HELP_INFO;
    }

    @Override
    public List<String> getPerms() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getTabCompletition(List<String> args) {
        return null;
    }

    @Override
    public void run(CommandSender sender, List<String> args) {
        sender.sendMessage("");
        sender.sendMessage(FormatUtils.color("&9&lWards Help &7Version &9" +
                Wards.getInstance().getDescription().getVersion()));

        CommandManager.getCmds().stream()
                .filter(absC -> {
                    for (String perms : absC.getPerms())
                        if (!sender.hasPermission(perms))
                            return false;
                    return true;
                })
                .forEach(absC -> sender.sendMessage(absC.getHelp()));

        sender.sendMessage("");
    }
}
