package io.github.divios.wards.menus;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.inventory.builder.inventoryPopulator;
import io.github.divios.core_lib.inventory.builder.paginatedGui;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.LocationUtils;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class wardListGui {

    public static Wards plugin = Wards.getInstance();

    private wardListGui() {}

    public static void prompt(Player p, List<Ward> wards) {

        paginatedGui.Builder()

                .withBackButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD)
                                .setName("&1&lNext")
                                .applyTexture("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf")
                        , 47)

                .withNextButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD)
                                .setName("&1&lNext")
                                .applyTexture("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf")
                        , 51)

                .withPopulator(inventoryPopulator.builder().ofGlass()
                        .mask("111111111")
                        .mask("100000001")
                        .mask("000000000")
                        .mask("000000000")
                        .mask("100000001")
                        .mask("111111111")
                        .scheme(11, 11, 3, 0, 0, 0, 3, 11, 11)
                        .scheme(11, 11)
                        .scheme(0)
                        .scheme(0)
                        .scheme(11, 11)
                        .scheme(11, 11, 3, 0, 0, 0, 3, 11, 11)
                )

                .withItems(
                        wards.stream()
                                .map(ward -> ItemButton.create(new ItemBuilder(ward.buildItem())
                                        .setName(ward.getName())
                                        .addLore("")
                                        .addLore("&8 - &7Type: &9" + ward.getType().getDisplay_name())
                                        .addLore("&8 - &7Radius: &9" + ward.getType().getRadius())
                                        .addLore("&8 - &7Location: &9" + LocationUtils.toString(ward.getCenter()))
                                        .addLore("", "&9Click to view more information"),
                                e -> ward.openInv((Player) e.getWhoClicked()))))

                .withTitle("&9Wards list of &f" + p.getName())
                .build().open(p);

    }
}
