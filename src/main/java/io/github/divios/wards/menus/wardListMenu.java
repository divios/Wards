package io.github.divios.wards.menus;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.inventory.builder.inventoryPopulator;
import io.github.divios.core_lib.inventory.builder.paginatedGui;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.LocationUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class wardListMenu {

    public static Wards plugin = Wards.getInstance();

    private final Player p;
    private final List<Ward> wards;

    private wardListMenu(Player p, List<Ward> wards) {
        this.p = p;
        this.wards = wards;

        init();
    }

    public static void prompt(Player p, List<Ward> wards) {
        new wardListMenu(p, wards);
    }

    private void init() {

        paginatedGui.Builder()

                .withBackButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD)
                                .setName(Wards.configManager.getGuiValues().LIST_PREVIOUS)
                                .applyTexture("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9")
                        , 47)

                .withNextButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD)
                                .setName(Wards.configManager.getGuiValues().LIST_NEXT)
                                .applyTexture("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf")
                        , 51)

                .withExitButton(ItemBuilder.of(XMaterial.OAK_DOOR).setName("&cReturn"),
                        e -> p.closeInventory(), 8)

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
                                                .addLorewithPlaces(Wards.configManager.getGuiValues().LIST_LORE,
                                                        s -> lorePlaces(s, ward)),
                                        e -> action(e, ward))))

                .withTitle(Msg.singletonMsg(Wards.configManager.getGuiValues().LIST_TITLE)
                        .add("\\{player}", p.getName()).build())
                .build().open(p);
    }

    private void action(InventoryClickEvent e, Ward ward) {

        if (WardsManager.getInstance().getWard(ward.getCenter()) == null) {     // If removed or expired while checking gui
            prompt(p, wards);
            return;
        }

        if (e.isLeftClick())
            ward.openInv((Player) e.getWhoClicked());

        else if (e.isRightClick()) {

            if (!p.hasPermission("wards.teleport")) {
                Msg.sendMsg(p, Wards.configManager.getLangValues().WARD_NO_PERMS);
                return;
            }

            Location wardLocation = ward.getCenter();

            Location safeLoc = LocationUtils
                    .getNearestSafeLocation(wardLocation.add(0, 1, 0), 8);

            if (safeLoc == null) {
                Msg.sendMsg(p, Wards.configManager.getGuiValues().LIST_NOT_SAFE);
                return;
            }

            p.closeInventory();
            Msg.sendMsg(p, Msg.singletonMsg(Wards.configManager.getLangValues().WARD_TELEPORT_COOLDOWN)
                    .add("\\{cooldown}",
                            String.valueOf(Wards.configManager.getConfigValues().WARD_TELEPORT_DELAY / 20D))
                    .build());
            LocationUtils.delayedTeleport(p, wardLocation, Wards.configManager.getConfigValues().WARD_TELEPORT_DELAY,
                    aBoolean -> {
                        if (aBoolean)
                            utils.playSound(p, XSound.ENTITY_ENDERMAN_TELEPORT);
                    });

        }
    }

    private String lorePlaces(String s, Ward ward) {

        if (s.equalsIgnoreCase("player"))
            return p.getName();

        else if (s.equalsIgnoreCase("type"))
            return ward.getType().getDisplay_name();

        else if (s.equalsIgnoreCase("radius"))
            return String.valueOf(ward.getType().getRadius());

        else if (s.equalsIgnoreCase("loc"))
            return LocationUtils.toString(ward.getCenter());

        return null;
    }
}
