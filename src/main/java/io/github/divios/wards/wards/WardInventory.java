package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.XSymbols;
import io.github.divios.wards.Wards;
import io.github.divios.wards.tasks.WardsShowTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class WardInventory {

    public static InventoryGUI build(Ward ward) {

        InventoryGUI builded = new InventoryGUI(Wards.getInstance(), 27, "&1&lWard Manager");

        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.EGG)
            .setName("&6Type: " + ward.getType().getType()), e -> {}), 4);

        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.CLOCK)
                        .setName("&a" + (ward.getTimer() == -1 ?
                                FormatUtils.color("&c&l" + XSymbols.TIMES_3) :
                                FormatUtils.formatTimeOffset(ward.getTimer() * 1000L))),
                e -> {})
                , 11);

        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.BARRIER).setName("&cRecoger ward"),
                e -> {
                    Optional.ofNullable(Bukkit.getPlayer(ward.getOwner()))
                            .ifPresent(o -> o.sendMessage(FormatUtils.color("&7Removiste tu ward")));
                    WardsManager.getInstance().deleteWard(ward);
                    ItemUtils.give((Player) e.getWhoClicked(), ward.buildItem(), 1);
                    e.getWhoClicked().closeInventory();
                }), 13);


        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.OAK_FENCE),
                e -> {
                    e.getWhoClicked().closeInventory();
                    WardsShowTask.generate((Player) e.getWhoClicked(), ward);
                }), 15);

        builded.setDestroyOnClose(false);

        return builded;
    }

}
