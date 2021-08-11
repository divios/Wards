package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.event.MergedSubscription;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.inventory.builder.inventoryPopulator;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.ChatPrompt;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.XSymbols;
import io.github.divios.core_lib.scheduler.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.menus.wardRolesMenu;
import io.github.divios.wards.tasks.WardsShowTask;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.ArrayList;

public class WardsMenu {

    private static final Wards plugin = Wards.getInstance();

    private final Ward ward;
    private InventoryGUI mainInv;
    private InventoryGUI settingsInv;

    private final Watcher watcher;

    public WardsMenu(Ward ward) {
        this.ward = ward;
        mainInv = build();
        settingsInv = createSettings();
        watcher = new Watcher(this);
    }

    protected Ward getWard() {
        return ward;
    }

    protected InventoryGUI getMainInv() {
        return mainInv;
    }

    protected InventoryGUI getSettingsInv() {
        return settingsInv;
    }

    private InventoryGUI build() {

        InventoryGUI builded = new InventoryGUI(plugin, 27, ward.getName());

        inventoryPopulator.builder()
                .ofGlass()
                .mask("111111111")
                .mask("110111011")
                .mask("111111111")
                .scheme(11, 11, 3, 7, 7, 7, 3, 11, 11)
                .scheme(11, 3, 7, 7, 7, 3, 11)
                .scheme(11, 11, 3, 7, 7, 7, 3, 11, 11)
                .apply(builded.getInventory());

        builded.addButton(ItemButton.create(ItemBuilder.of(Wards.configManager.getGuiValues().TIME_MATERIAL)
                        .setName(Wards.configManager.getGuiValues().TIME_NAME)
                        .addLore(Msg.msgList(Wards.configManager.getGuiValues().TIME_LORE)
                                .add("\\{time}", (ward.getTimer() == -1 ?
                                        FormatUtils.color("&c&l" + XSymbols.TIMES_3.parseSymbol()) :
                                        FormatUtils.formatTimeOffset(ward.getTimer() * 1000L) + "")).build()),
                e -> {
                }), 11);


        builded.addButton(ItemButton.create(ItemBuilder.of(ward.getType().buildItem())
                        .setLore(Msg.msgList(Wards.configManager.getGuiValues().TYPE_LORE)
                                .add("\\{type}", "" + ward.getType().getType())
                                .add("\\{radius}", ward.getType().getRadius() + "")
                                .build())
                , e -> {
                }), 13);


        builded.addButton(ItemButton.create(ItemBuilder.of(Wards.configManager.getGuiValues().SHOW_MATERIAL)
                        .setName(Wards.configManager.getGuiValues().SHOW_NAME).addLore(Wards.configManager.getGuiValues().SHOW_LORE),
                e -> {
                    e.getWhoClicked().closeInventory();
                    WardsShowTask.generate((Player) e.getWhoClicked(), ward);
                }), 15);

        builded.addButton(ItemButton.create(ItemBuilder.of(Wards.configManager.getGuiValues().SETTINGS_MATERIAL)
                        .setName(Wards.configManager.getGuiValues().SETTINGS_NAME)
                        .addLore(Wards.configManager.getGuiValues().SETTINGS_LORE),
                e -> settingsInv.open((Player) e.getWhoClicked())), 26);

        builded.setDestroyOnClose(false);

        return builded;
    }

    private InventoryGUI createSettings() {

        InventoryGUI builded = new InventoryGUI(plugin, 27, ward.getName());

        inventoryPopulator.builder()
                .ofGlass()
                .mask("111111111")
                .mask("110111011")
                .mask("111111111")
                .scheme(11, 11, 3, 7, 7, 7, 3, 11, 11)
                .scheme(11, 3, 7, 7, 7, 3, 11)
                .scheme(11, 11, 3, 7, 7, 7, 3, 11, 11)
                .apply(builded.getInventory());


        builded.addButton(ItemButton.create(ItemBuilder.of(Wards.configManager.getGuiValues().CHANGE_NAME_MATERIAL)
                        .setName(Wards.configManager.getGuiValues().CHANGE_NAME_NAME)
                        .addLore(Wards.configManager.getGuiValues().CHANGE_NAME_LORE),
                e -> {

                    Player p = (Player) e.getWhoClicked();

                    if (!p.hasPermission("wards.admin") &&
                            !p.getUniqueId().equals(ward.getOwner())) {
                        Msg.sendMsg(p, Wards.configManager.getLangValues().WARD_NO_PERMS);
                        return;
                    }

                    ChatPrompt.builder()
                            .withPlayer(p)
                            .withResponse(s -> {

                                if (s.isEmpty()) {
                                    settingsInv.open(p);
                                    Msg.sendMsg(p, Wards.configManager.getGuiValues().CHANGE_NAME_NOT_EMPTY);
                                }

                                ward.setName(s);
                                destroy();
                                Schedulers.sync().run(() -> {
                                    mainInv = build();
                                    settingsInv = createSettings();
                                    settingsInv.open(p);
                                });
                            })
                            .withCancel(cancelReason -> {
                                if (WardsManager.getInstance().getWard(ward.getCenter()) == null) return;
                                Schedulers.sync().run(() -> settingsInv.open(p));
                            })
                            .withTitle(Wards.configManager.getGuiValues().CHANGE_NAME_TITLE)
                            .withSubtitle(Wards.configManager.getGuiValues().CHANGE_NAME_SUBTITLE)
                            .prompt();

                }), 11);

        builded.addButton(ItemButton.create(ItemBuilder.of(XMaterial.BLACK_BANNER)
                        .setName(Wards.configManager.getGuiValues().ROLES_NAME)
                        .setLore(Wards.configManager.getGuiValues().ROLES_LORE),
                e -> {
                    wardRolesMenu.prompt((Player) e.getWhoClicked(), ward);
                }
        ), 15);

        builded.addButton(ItemButton.create(ItemBuilder.of(Wards.configManager.getGuiValues().MUTE_MATERIAL)
                        .setName(Wards.configManager.getGuiValues().MUTE_NAME)
                        .setLore(Wards.configManager.getGuiValues().MUTE_LORE),
                e -> {
                    if (ward.getMuted().contains(e.getWhoClicked().getUniqueId())) {
                        ward.removeMuted((Player) e.getWhoClicked());
                        Msg.sendMsg((Player) e.getWhoClicked(), "&7You'll now receive pings from this ward");
                    } else {
                        ward.addmuted((Player) e.getWhoClicked());
                        Msg.sendMsg((Player) e.getWhoClicked(), "&7You'll no longer receive pings from this ward");
                    }
                })
                , 8);

        builded.addButton(ItemButton.create(ItemBuilder.of(Wards.configManager.getGuiValues().RETURN_MATERIAL)
                        .setName(Wards.configManager.getGuiValues().RETURN_NAME)
                        .addLore(Wards.configManager.getGuiValues().RETURN_LORE),
                e -> mainInv.open((Player) e.getWhoClicked())), 26);

        builded.setDestroyOnClose(false);

        return builded;
    }

    public void show(Player p) {
        mainInv.open(p);
    }

    public void destroy() {

        Schedulers.sync().run(() -> {            // Close invs sync
            new ArrayList<>(mainInv.getInventory().getViewers())
                    .forEach(HumanEntity::closeInventory);

            new ArrayList<>(settingsInv.getInventory().getViewers())
                    .forEach(HumanEntity::closeInventory);
        });

        mainInv.destroy();
        settingsInv.destroy();
        watcher.destroy();

    }

    private static final class Watcher {

        private final WardsMenu inv;
        private Task update;
        private MergedSubscription listeners;

        protected Watcher(WardsMenu inv) {
            this.inv = inv;

            if (inv.getWard().getTimer() != -1)
                initListeners();

        }

        private void initListeners() {

            listeners = Events.merge(InventoryEvent.class, InventoryCloseEvent.class, InventoryOpenEvent.class)
                    .filter(e -> e.getInventory().equals(inv.getMainInv().getInventory()))
                    .handler(e -> {


                        if ((e instanceof InventoryOpenEvent)) {

                            if (e.getViewers().size() != 1) return;

                            update = Schedulers.builder().sync().every(20).run(() -> {

                                inv.getMainInv().getInventory().setItem(11,
                                        new ItemBuilder(Wards.configManager.getGuiValues().TIME_MATERIAL)
                                                .setName(Wards.configManager.getGuiValues().TIME_NAME)
                                                .addLore(Msg.msgList(Wards.configManager.getGuiValues().TIME_LORE)
                                                        .add("\\{time}", FormatUtils.formatTimeOffset(
                                                                inv.getWard().getTimer() * 1000L) + "").build()));
                            });

                        } else if ((e instanceof InventoryCloseEvent)) {

                            if (e.getViewers().size() - 1 != 0) return;
                            update.close();
                            //Bukkit.broadcastMessage("destroyer listener");
                        }

                    });
        }

        protected void destroy() {
            if (update != null) update.close();
            if (listeners != null) listeners.unregister();
        }

    }

}
