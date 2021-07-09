package io.github.divios.wards.menus;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.LocationUtils;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.Ward;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class wardListGui {

    public static Wards plugin = Wards.getInstance();

    private final Player p;
    private final List<Ward> wards;
    private final List<InventoryGUI> invs;

    private wardListGui(Player p, List<Ward> wards) {
        this.p = p;
        this.wards = wards;
        invs = new ArrayList<>();

        open();
    }

    public static void prompt(Player p, List<Ward> wards) {
        new wardListGui(p, wards);
    }

    private void open() {

        IntStream.range(0, wards.isEmpty() ? 1 : (int) Math.ceil(wards.size() / 32D))
                .forEach(value -> invs.add(new InventoryGUI(plugin, 54,
                        "&9Wards list of &f" + p.getName())));

        final int[] sum = {0};

        invs.forEach(inventoryGUI -> {
            inventoryGUI.setDestroyOnClose(false);

            IntStream.of(0, 1, 9, 7, 8, 17, 45, 46, 36, 52, 53, 44).forEach(value ->
                    inventoryGUI.addButton(new ItemButton(
                            new ItemBuilder(XMaterial.BLUE_STAINED_GLASS_PANE)
                                    .setName("&c"), e -> {
                    }), value));

            IntStream.of(2, 6, 47, 51).forEach(value ->
                    inventoryGUI.addButton(new ItemButton(
                            new ItemBuilder(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE)
                                    .setName("&c"), e -> {
                    }), value));

            IntStream.of(3, 4, 5, 48, 49, 50).forEach(value ->
                    inventoryGUI.addButton(new ItemButton(
                            new ItemBuilder(XMaterial.WHITE_STAINED_GLASS_PANE)
                                    .setName("&c"), e -> {
                    }), value));

            int index = invs.indexOf(inventoryGUI);
            if (index != invs.size() - 1) {      // next buttom
                inventoryGUI.addButton(new ItemButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD)
                                .setName("&1&lNext").applyTexture("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf"),
                        e -> invs.get(index + 1).open(p)), 51);
            }

            if (index != 0) {                   // previous buttom
                inventoryGUI.addButton(new ItemButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD)
                                .setName("&1&lPrevious").applyTexture("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9"),
                        e -> invs.get(index - 1).open(p)), 47);
            }

            inventoryGUI.addButton(new ItemButton(new ItemBuilder(XMaterial.PLAYER_HEAD)
                    .setName("&cExit").setLore("&7Click to exit")
                    .applyTexture("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf")
                    , e -> {
                p.closeInventory();
            }), 8);

            for (int i = 0; i < 54; i++) {
                if (sum[0] >= wards.size()) break;

                Ward ward = wards.get(sum[0]);
                if (!ItemUtils.isEmpty(inventoryGUI.getInventory().getItem(i))) continue;

                inventoryGUI.addButton(ItemButton.create(new ItemBuilder(ward.buildItem())
                        .setName(ward.getName())
                        .addLore("")
                        .addLore("&8 - &7Type: &9" + ward.getType().getDisplay_name())
                        .addLore("&8 - &7Radius: &9" + ward.getType().getRadius())
                        .addLore("&8 - &7Location: &9" + LocationUtils.toString(ward.getCenter()))
                        .addLore("", "&9Click to view more information")
                        , e-> ward.openInv((Player) e.getWhoClicked()) ), i);

                sum[0]++;
            }
        });

        invs.get(0).open(p);
    }

}
