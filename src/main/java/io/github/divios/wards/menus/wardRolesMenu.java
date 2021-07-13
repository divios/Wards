package io.github.divios.wards.menus;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.inventory.builder.inventoryPopulator;
import io.github.divios.core_lib.inventory.builder.paginatedGui;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.ChatPrompt;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.Ward;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class wardRolesMenu {

    private final Player p;
    private final Ward ward;

    private paginatedGui pGui;

    private wardRolesMenu(Player p, Ward ward) {
        this.p = p;
        this.ward = ward;

        build();
    }

    public static void prompt(Player p, Ward ward) {
        new wardRolesMenu(p, ward);
    }

    private void build() {

        pGui = paginatedGui.Builder()
                .withBackButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD)
                                .setName(Wards.guiValues.LIST_PREVIOUS)
                                .applyTexture("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9")
                        , 47)

                .withNextButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD)
                                .setName(Wards.guiValues.LIST_NEXT)
                                .applyTexture("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf")
                        , 51)

                .withExitButton(ItemBuilder.of(XMaterial.OAK_DOOR).setName("&cReturn"),
                        e -> { Schedulers.sync().run(() -> ward.openInv(p)); }, 8)

                .withButtons((inventoryGUI, integer) -> {
                    inventoryGUI.addButton(ItemButton.create(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                        .applyTexture("3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716")
                        .setName("&9Add player").addLore("&7Click to add player"),
                            e -> {
                                ChatPrompt.prompt(p, s -> {
                                    OfflinePlayer op = Bukkit.getOfflinePlayer(s);
                                    if (!op.hasPlayedBefore()) {
                                        Msg.sendMsg(p, "That player doesn't exist");
                                    }
                                    else ward.addAccepted(op.getUniqueId());
                                    refresh();

                                }, cancelReason -> refresh(), "", "");
                            }), 53);
                })

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
                .withItems(ward.getAcceptedP().stream()
                        .map(uuid -> ItemButton.create(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                                .setName(Bukkit.getOfflinePlayer(uuid).getName())
                                .applyTexture(uuid), e -> {}))
                )

                .withTitle("roles")
                .build();

        pGui.open(p);

    }

    private void refresh() {
        Schedulers.sync().run(() -> {
            prompt(p, ward);
            pGui.destroy();
        });
    }


}
