package io.github.divios.wards.menus;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.inventory.builder.inventoryPopulator;
import io.github.divios.core_lib.inventory.builder.paginatedGui;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.ChatPrompt;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.confirmIH;
import io.github.divios.core_lib.profiles.Profile;
import io.github.divios.core_lib.profiles.ProfileRepository;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.Ward;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

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

        BiMap<String, UUID> players = HashBiMap.create();
        ward.getAcceptedP().forEach(uuid -> {
            players.put(Bukkit.getOfflinePlayer(uuid).getName(), uuid);
        });

        pGui = paginatedGui.Builder()
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

                .withExitButton(ItemBuilder.of(XMaterial.OAK_DOOR).setName(Wards.configManager.getGuiValues().ROLES_RETURN),
                        e -> {
                            Schedulers.sync().run(() -> ward.openInv(p));
                        }, 8)

                .withButtons((inventoryGUI, integer) -> {
                    inventoryGUI.addButton(ItemButton.create(ItemBuilder.of(XMaterial.PLAYER_HEAD)
                                    .applyTexture("3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716")
                                    .setName(Wards.configManager.getGuiValues().ROLES_ADD_NAME)
                                    .addLore(Wards.configManager.getGuiValues().ROLES_ADD_LORE),
                            e -> {
                                ChatPrompt.prompt(p, s -> {
                                    OfflinePlayer op = Bukkit.getOfflinePlayer(s);
                                    if (!op.hasPlayedBefore()) {
                                        Msg.sendMsg(p, Wards.configManager.getLangValues().WARD_PLAYER_NOTEXITS);
                                    } else ward.addAccepted(op.getUniqueId());
                                    refresh();

                                }, cancelReason -> refresh(),
                                        Wards.configManager.getGuiValues().ROLES_PROMPT_TITLE,
                                        Wards.configManager.getGuiValues().ROLES_PROMPT_SUBTITLE);
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
                                        .setName(players.inverse().get(uuid))
                                        .applyTexture(uuid),
                                e -> {

                                    if (!p.getUniqueId().equals(ward.getOwner())) {
                                        Msg.sendMsg(p, Wards.configManager.getLangValues().WARD_NO_PERMS);
                                        return;
                                    }

                                    if (players.get(e.getCurrentItem().getItemMeta().getDisplayName())
                                            .equals(ward.getOwner())) {
                                        Msg.sendMsg(p, Wards.configManager.getLangValues().WARD_OWNER_DENY_REMOVE);
                                        return;
                                    }

                                    confirmIH.builder()
                                            .withPlayer(p)
                                            .withItem(e.getCurrentItem())
                                            .withAction((p1, ba) -> {
                                                if (ba)
                                                    ward.removeAccepted(players.get(e.getCurrentItem()
                                                            .getItemMeta().getDisplayName()));
                                                Schedulers.sync().run(this::refresh);
                                            })
                                            .withTitle(Wards.configManager.getGuiValues().CONFIRM_TITLE)
                                            .withConfirmLore(Wards.configManager.getGuiValues().CONFIRM_YES)
                                            .withCancelLore(Wards.configManager.getGuiValues().CONFIRM_NO)
                                            .prompt();

                                }))
                )

                .withTitle(Wards.configManager.getGuiValues().ROLES_TITLE)
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
