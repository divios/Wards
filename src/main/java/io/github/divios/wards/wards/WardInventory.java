package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.event.MergedSubscription;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.ChatPrompt;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.XSymbols;
import io.github.divios.core_lib.scheduler.Task;
import io.github.divios.wards.Wards;
import io.github.divios.wards.tasks.WardsShowTask;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class WardInventory {

    private static final Wards plugin = Wards.getInstance();

    private final Ward ward;
    private InventoryGUI mainInv;
    private InventoryGUI settingsInv;

    private final Watcher watcher;

    public WardInventory(Ward ward) {
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

        IntStream.of(0, 1, 7, 8, 9, 17, 18, 19, 25).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.GLASS_PANE_1)
                    .setName("&c"), e -> {
            }), value);
        });

        IntStream.of(2, 6, 10, 16, 20, 24).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.GLASS_PANE_2)
                    .setName("&c"), e -> {
            }), value);
        });

        IntStream.of(3, 4, 5, 12, 14, 21, 22, 23).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.GLASS_PANE_3)
                    .setName("&c"), e -> {
            }), value);
        });

        builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.TIME_MATERIAL)
                        .setName(Wards.guiValues.TIME_NAME)
                        .addLore(Msg.msgList(Wards.guiValues.TIME_LORE)
                                .add("\\{time}", (ward.getTimer() == -1 ?
                                        FormatUtils.color("&c&l" + XSymbols.TIMES_3.parseSymbol()) :
                                        FormatUtils.formatTimeOffset(ward.getTimer() * 1000L) + "")).build()),
                e -> {
                })
                , 11);


        builded.addButton(ItemButton.create(new ItemBuilder(ward.getType().buildItem())
                        .setLore(Msg.msgList(Wards.guiValues.TYPE_LORE)
                                .add("\\{type}", "" + ward.getType().getType())
                                .add("\\{radius}", ward.getType().getRadius() + "")
                                .build())
                , e -> {
                }), 13);


        builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.SHOW_MATERIAL)
                        .setName(Wards.guiValues.SHOW_NAME).addLore(Wards.guiValues.SHOW_LORE),
                e -> {
                    e.getWhoClicked().closeInventory();
                    WardsShowTask.generate((Player) e.getWhoClicked(), ward);
                }), 15);

        builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.SETTINGS_MATERIAL)
                        .setName(Wards.guiValues.SETTINGS_NAME)
                        .addLore(Wards.guiValues.SETTINGS_LORE),
                e -> settingsInv.open((Player) e.getWhoClicked())), 26);

        builded.setDestroyOnClose(false);

        return builded;
    }

    private InventoryGUI createSettings() {
        InventoryGUI builded = new InventoryGUI(plugin, 27, ward.getName());

        IntStream.of(0, 1, 7, 8, 9, 17, 18, 19, 25).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.BLUE_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {
            }), value);
        });

        IntStream.of(2, 6, 10, 15, 16, 20, 24).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {
            }), value);
        });

        IntStream.of(3, 4, 5, 12, 13, 14, 21, 22, 23).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {
            }), value);
        });

        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.PAPER)
                        .setName(Wards.guiValues.CHANGE_NAME_NAME)
                        .addLore(Wards.guiValues.CHANGE_NAME_LORE),
                e -> {

                    Player p = (Player) e.getWhoClicked();
                    ChatPrompt.prompt(plugin, p, s -> {
                        if (s.isEmpty()) {
                            settingsInv.open(p);
                            Msg.sendMsg(p, Wards.guiValues.CHANGE_NAME_NOT_EMPTY);
                        }

                        ward.setName(s);
                        destroy();
                        Schedulers.sync().run(() -> {
                            mainInv = build();
                            settingsInv = createSettings();
                            settingsInv.open(p);
                        });

                    }, motive -> {

                        if (WardsManager.getInstance().getWard(ward.getCenter()) == null) return;
                        Schedulers.sync().run(() -> settingsInv.open(p));

                    }, Wards.guiValues.CHANGE_NAME_TITLE, Wards.guiValues.CHANGE_NAME_SUBTITLE);

                }), 11);

        builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.RETURN_MATERIAL)
                        .setName(Wards.guiValues.RETURN_NAME)
                        .addLore(Wards.guiValues.RETURN_LORE),
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

    public static class Watcher {

        private final WardInventory inv;
        private Task update;
        private MergedSubscription listeners;

        protected Watcher(WardInventory inv) {
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

                            //Bukkit.broadcastMessage("Iniciado listener");
                            update = Schedulers.builder().sync().every(20).run(() -> {

                                    //Bukkit.broadcastMessage("updated");
                                    inv.getMainInv().getInventory().setItem(11,
                                    new ItemBuilder(Wards.guiValues.TIME_MATERIAL)
                                    .setName(Wards.guiValues.TIME_NAME)
                                    .addLore(Msg.msgList(Wards.guiValues.TIME_LORE)
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
